package com.nyansapoai.teaching.presentation.assessments.numeracy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationMetadata
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.assessments.components.checkAnswer
import com.nyansapoai.teaching.utils.ResultStatus
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class NumeracyAssessmentViewModel(
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository,
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NumeracyAssessmentState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NumeracyAssessmentState()
        )


    fun onAction(action: NumeracyAssessmentAction) {
        when (action) {
            is NumeracyAssessmentAction.OnCaptureAnswer -> {
                println("Captured answer image: ${action.imageByteArray.size} bytes")
                _state.value = _state.value.copy(
                    answerImageByteArray = action.imageByteArray,
//                    shouldCaptureAnswer = false
                )
            }
            is NumeracyAssessmentAction.OnCaptureWorkArea -> {
                println("Captured work area image: ${action.imageByteArray.size} bytes")
                _state.value = _state.value.copy(
                    workAreaImageByteArray = action.imageByteArray,
                )
            }
            NumeracyAssessmentAction.OnClearAnswer -> {
                _state.value = _state.value.copy(
                    answerImageByteArray = null,
                    shouldCaptureAnswer = false
                )
            }
            NumeracyAssessmentAction.OnClearWorkArea -> {
                _state.value = _state.value.copy(
                    workAreaImageByteArray = null,
                    shouldCaptureWorkArea = false
                )
            }
            is NumeracyAssessmentAction.OnShouldCaptureAnswerChange -> {
                _state.value = _state.value.copy(
                    shouldCaptureAnswer = action.shouldCapture
                )
            }
            is NumeracyAssessmentAction.OnShouldCaptureWorkAreaChange -> {
                _state.value = _state.value.copy(
                    shouldCaptureWorkArea = action.shouldCapture
                )
            }
            NumeracyAssessmentAction.OnSubmitAnswer -> {
                submitAnswer()
            }

            is NumeracyAssessmentAction.OnAddArithmeticOperation -> {

                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("NumeracyAssessmentViewModel", "Processing arithmetic operation: ${action.numeracyOperations}")

                    val imageByteArray = _state.value.answerImageByteArray
                    if (_state.value.answerImageByteArray == null || _state.value.response == null || _state.value.responseError != null || _state.value.answerUri == null) {
                        println("No answer image captured.")
                        _state.value = _state.value.copy(
                            shouldCaptureAnswer = false,
                            shouldCaptureWorkArea = false,
                            showResponseAlert = true,
                            responseError = "No answer image captured. Please try again."
                        )
                        return@launch
                    }

                    try {


                        // Create and add the arithmetic operation
                        val arithmeticOperation = NumeracyArithmeticOperation(
                            type = action.numeracyOperations.operationType.name,
                            expected_answer = action.numeracyOperations.answer,
                            student_answer = _state.value.response,
                            operationNumber1 = action.numeracyOperations.firstNumber,
                            operationNumber2 = action.numeracyOperations.secondNumber,
                            metadata = NumeracyOperationMetadata(
                                workAreaMediaUrl = null,
                                answerMediaUrl = _state.value.answerUri,
                                passed = checkAnswer(
                                    answer = _state.value.response,
                                    correctAnswer = action.numeracyOperations.answer
                                )
                            )
                        )

                        // Update state with the new operation and reset capture state
                        _state.value = _state.value.copy(
                            arithmeticOperationResults = _state.value.arithmeticOperationResults.apply {
                                add(arithmeticOperation)
                            },
                            answerImageByteArray = null,
                            workAreaImageByteArray = null,
                            shouldCaptureAnswer = false,
                            shouldCaptureWorkArea = false,
                            showResponseAlert = false,
                            answerInt = null,
                            answerUri = null
                        )

                        action.onSuccess()
                        println("Added arithmetic operation: ${_state.value.arithmeticOperationResults}")
                    } catch (e: Exception) {
                        Log.e("NumeracyAssessmentViewModel", "Error processing image recognition: ${e.message}", e)
                        _state.value = _state.value.copy(
                            shouldCaptureAnswer = false,
                            shouldCaptureWorkArea = false,
                            showResponseAlert = true,
                            responseError = "An error occurred: ${e.message ?: "Unknown error"}"
                        )
                    }
                }
            }

            is NumeracyAssessmentAction.OnAddCountMatch -> {

                if (_state.value.countMatchAnswer == null) {
                    _state.value = _state.value.copy(
                        showResponseAlert = true,
                        responseError = "Count match answer is required."
                    )
                    return
                }


                val countMatchItem = CountMatch(
                    type = "Count and Match",
                    expected_number = action.countMatch,
                    student_count = _state.value.countMatchAnswer,
                    passed = checkAnswer(
                        answer = _state.value.countMatchAnswer,
                        correctAnswer = action.countMatch
                    )
                )

                _state.value = _state.value.copy(
                    countAndMatchResults = _state.value.countAndMatchResults.apply { add(countMatchItem) },
                    isLoading = true,
                    showResponseAlert = false,
                    responseError = null
                )

                action.onSuccess.invoke()

                _state.update { it.copy(isLoading = false,countMatchAnswer = null,  ) }

            }
            is NumeracyAssessmentAction.OnSubmitCountMatch -> {
                submitCountAndMatch(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    countMatchList = _state.value.countAndMatchResults,
                    onSuccess = action.onSuccess
                )
            }
            is NumeracyAssessmentAction.OnSubmitNumberRecognition -> TODO()
            is NumeracyAssessmentAction.OnSubmitNumeracyOperations -> {
                submitNumeracyArithmeticOperations(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    operationList = _state.value.arithmeticOperationResults,
                    onSuccess = action.onSuccess
                )
            }
            is NumeracyAssessmentAction.OnSubmitWordProblem -> {

                if (_state.value.wordProblem == null) {
                    _state.value = _state.value.copy(
                        showResponseAlert = true,
                        responseError = "Word problem is required."
                    )
                    return
                }

                if (_state.value.wordProblem != null){
                    submitNumeracyWordProblem(
                        assessmentId = action.assessmentId,
                        studentId = action.studentId,
                        wordProblem = _state.value.wordProblem!!,
                        onSuccess = action.onSuccess
                    )
                }

            }
            is NumeracyAssessmentAction.OnAdditionIndexChange -> {
                _state.value = _state.value.copy(
                    additionIndex = action.index
                )
            }
            is NumeracyAssessmentAction.OnCountMachIndexChange -> {
                _state.value = _state.value.copy(
                    countMatchIndex = action.index
                )
                println("Count match index updated: ${_state.value.countMatchIndex}")
            }
            is NumeracyAssessmentAction.OnDivisionIndexChange -> {
                _state.value = _state.value.copy(
                    divisionIndex = action.index
                )
            }
            is NumeracyAssessmentAction.OnMultiplicationIndexChange -> {
                _state.value = _state.value.copy(
                    multiplicationIndex = action.index
                )
            }
            is NumeracyAssessmentAction.OnNumberRecognitionIndexChange -> {
                _state.value = _state.value.copy(
                    numberRecognitionIndex = action.index
                )
            }
            is NumeracyAssessmentAction.OnSubtractionIndexChange -> {
                _state.value = _state.value.copy(
                    subtractionIndex = action.index
                )
            }

            is NumeracyAssessmentAction.OnNumeracyLevelChange -> {
                _state.value = _state.value.copy(
                    numeracyLevel = action.numeracyLevel
                )
            }

            is NumeracyAssessmentAction.OnAddNumberRecognition -> TODO()
            is NumeracyAssessmentAction.OnShowResponseAlertChange -> {
                _state.update { it.copy(
                    showResponseAlert = action.showResponseAlert
                ) }
            }

            is NumeracyAssessmentAction.OnReadAnswerImage -> {
                _state.value = _state.value.copy(
                    shouldCaptureAnswer = true
                )

                if (_state.value.answerImageByteArray == null) {
                    println("No answer image to read.")
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        artificialIntelligenceRepository.recognizeImage(imageByteArray = _state.value.answerImageByteArray!!)
                            .catch { e ->
                                _state.value = _state.value.copy(
                                    showResponseAlert = true,
                                    shouldCaptureAnswer = false,
                                    shouldCaptureWorkArea = false,
                                    response = null,
                                    responseError = e.message ?: "Error recognizing the answer image",
                                )
                            }
                            .collect { result ->

                                result.data?.let {
                                    _state.value = _state.value.copy(
                                        answerUri = result.data.url,
                                        answerInt = result.data.response,
                                        response = result.data.response,
                                        showResponseAlert = true,
                                        responseError = null
                                    )

                                } ?: run {
                                    _state.value = _state.value.copy(
                                        showResponseAlert = true,
                                        shouldCaptureAnswer = false,
                                        shouldCaptureWorkArea = false,
                                        response = null,
                                        responseError = "No data in vision recognition response, try again"
                                    )
                                }
                            }
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            showResponseAlert = true,
                            shouldCaptureAnswer = false,
                            shouldCaptureWorkArea = false,
                            response = null,
                            responseError = e.message ?: "Error processing the answer image"
                        )
                    }

                }

            }

            is NumeracyAssessmentAction.OnCountMatchAnswerChange -> {
                _state.value = _state.value.copy(
                    countMatchAnswer = action.countMatchAnswer
                )

                println("Count match answer updated: ${_state.value.countMatchAnswer}")
            }
        }
    }


    private val _answerImageByteArrayState = MutableStateFlow<Results<VisionRecognition>>(Results.initial())
    val answerImageByteArrayState = _answerImageByteArrayState.asStateFlow()

    private val _workAreaImageByteArrayState = MutableStateFlow<Results<VisionRecognition>>(Results.initial())
    val workAreaImageByteArrayState = _workAreaImageByteArrayState.asStateFlow()

    private fun submitAnswer() {
        _state.value = _state.value.copy(
            shouldCaptureAnswer = true,
            shouldCaptureWorkArea = true
        )

        if  (_state.value.workAreaImageByteArray == null) {
            println("No answer image captured.")
            return
        }

        _state.value.workAreaImageByteArray?.let { imageByteArray ->
            viewModelScope.launch() {
                artificialIntelligenceRepository.recognizeImage(imageByteArray = imageByteArray )
                    .catch {  _workAreaImageByteArrayState.value = Results.error(msg = it.message ?: "Error  recognizing the answer") }
                    .collect { answer ->
                        _workAreaImageByteArrayState.value = answer
                    }
            }
        }


        _state.value.answerImageByteArray?.let { imageByteArray ->
            viewModelScope.launch(Dispatchers.IO) {
                artificialIntelligenceRepository.recognizeImage(imageByteArray = imageByteArray )
                    .catch {  _answerImageByteArrayState.value = Results.error(msg = it.message ?: "Error  recognizing the work area") }
                    .collect { workArea ->
                        _answerImageByteArrayState.value = workArea

                    }
            }
        }
    }

    private fun submitCountAndMatch(
        assessmentId: String,
        studentId: String,
        countMatchList: List<CountMatch>,
        onSuccess: () -> Unit
    ) {
        if (countMatchList.isEmpty()) {
            println("No count and match results to submit.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = assessmentRepository.assessNumeracyCountAndMatch(
                countAndMatchList = countMatchList,
                studentID = studentId,
                assessmentId = assessmentId
            )

            when(result.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    onSuccess.invoke()
                }
                ResultStatus.ERROR -> {
                    println("can not add count and match")
                }
            }


        }
    }

    private fun submitNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String,
        operationList: List<NumeracyArithmeticOperation>,
        onSuccess: () -> Unit = {}
    ){
        if (operationList.isEmpty()){
            return
        }

        viewModelScope.launch {
            val result = assessmentRepository.assessNumeracyArithmeticOperations(
                assessmentId = assessmentId,
                studentID = studentId,
                arithmeticOperations = operationList,
            )

            when(result.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    onSuccess.invoke()
                }
                ResultStatus.ERROR -> {
                    println("can not add operation arithmetic")
                }
            }


        }
    }

    private fun readHardwrittenAnswer(){

    }


    private fun submitNumeracyWordProblem(
        assessmentId: String,
        studentId: String,
        wordProblem: NumeracyWordProblem,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = assessmentRepository.assessNumeracyWordProblem(
                wordProblem = wordProblem,
                studentID = studentId,
                assessmentId = assessmentId
            )

            when(result.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    onSuccess.invoke()
                }
                ResultStatus.ERROR -> {
                    println("can not add word problem")
                }
            }
        }
    }
}