package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.assessments.components.checkAnswer
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.EvaluateReadingAssessmentWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker
import com.nyansapoai.teaching.utils.ResultStatus
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import kotlin.text.compareTo
import kotlin.text.get

class NumeracyAssessmentViewModel(
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository,
    private val assessmentRepository: AssessmentRepository,
    private val mediaRepository: MediaRepository,
    private val localDataSource: LocalDataSource,
    private val workManager: WorkManager
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
            /*
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
            }*/

            NumeracyAssessmentAction.OnClearAnswer -> {
                _state.value = _state.value.copy(
//                    answerImageByteArray = null,
                    answerFilePath = null,
                    workAreaFilePath = null,
                    shouldCaptureAnswer = false
                )
            }
            NumeracyAssessmentAction.OnClearWorkArea -> {
                _state.value = _state.value.copy(
//                    workAreaImageByteArray = null,
//                    shouldCaptureWorkArea = false
                )
            }
            is NumeracyAssessmentAction.OnIsSubmittingChange -> {
                _state.update {
                    it.copy(
                        shouldCaptureAnswer = action.isSubmitting
                    )
                }
            }
            /*
            is NumeracyAssessmentAction.OnShouldCaptureWorkAreaChange -> {
                _state.value = _state.value.copy(
                    shouldCaptureWorkArea = action.shouldCapture
                )
            }*/
            NumeracyAssessmentAction.OnSubmitAnswer -> {
//                submitAnswer()
            }
            is NumeracyAssessmentAction.OnAddArithmeticOperation -> {

                /*
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("NumeracyAssessmentViewModel", "Processing arithmetic operation: ${action.numeracyOperations}")

                    val imageByteArray = _state.value.answerImageByteArray
                    if (_state.value.answerImageByteArray == null || _state.value.response == null || _state.value.error != null || _state.value.answerUri == null) {
                        println("No answer image captured.")
                        _state.value = _state.value.copy(
                            shouldCaptureAnswer = false,
                            shouldCaptureWorkArea = false,
                            showResponseAlert = true,
                            error = "No answer image captured. Please try again."
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
//                            answerImageByteArray = null,
//                            workAreaImageByteArray = null,
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
                            error = "An error occurred: ${e.message ?: "Unknown error"}"
                        )
                    }
                }

                 */
            }
            /*
            is NumeracyAssessmentAction.OnAddCountMatch -> {
                if (_state.value.countMatchAnswer == null) {
                    _state.value = _state.value.copy(
                        showResponseAlert = true,
                        error = "Count match answer is required."
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
                    error = null
                )

                action.onSuccess.invoke()

                _state.update { it.copy(isLoading = false,countMatchAnswer = null,  ) }

            }
            */
            is NumeracyAssessmentAction.OnSubmitCountMatch -> {
                submitCountAndMatch(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    onSuccess = action.onSuccess
                )
            }
            is NumeracyAssessmentAction.OnSubmitNumberRecognition -> TODO()
            is NumeracyAssessmentAction.OnSubmitNumeracyOperations -> {
                submitNumeracyArithmeticOperations(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    onSuccess = action.onSuccess
                )
            }
            is NumeracyAssessmentAction.OnSubmitWordProblem -> {

                if (_state.value.wordProblem == null) {
                    _state.value = _state.value.copy(
                        showResponseAlert = true,
                        error = "Word problem is required."
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
            /*
            is NumeracyAssessmentAction.OnReadAnswerImage -> {
                _state.value = _state.value.copy(
                    shouldCaptureAnswer = true
                )

                if (_state.value.answerImageByteArray == null) {
                    println("No answer image to read.")
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    val response = mediaRepository.saveImage(imageByteArray = _state.value.answerImageByteArray!!)

                    when(response.status){
                        ResultStatus.INITIAL ,
                        ResultStatus.LOADING -> {
                            println("Firebase storage is loading...")
                        }
                        ResultStatus.SUCCESS -> {
                            println("Firebase storage success: ${response.data}")

                            if (response.data == null){
                                return@launch
                            }

                            try {
                                artificialIntelligenceRepository.textExtractionFromImage(request = GetTextFromImageRequestDTO(url = response.data ))
                                    .catch { e ->
                                        _state.value = _state.value.copy(
                                            showResponseAlert = true,
                                            shouldCaptureAnswer = false,
                                            shouldCaptureWorkArea = false,
                                            response = null,
                                            error = e.message ?: "Error recognizing the answer image",
                                        )
                                    }
                                    .collect { result ->

                                        println("Vision recognition result: $result")

                                        result.data?.let {
                                            _state.value = _state.value.copy(
                                                answerUri = result.data.url,
                                                answerInt = result.data.response,
                                                response = result.data.response,
                                                showResponseAlert = true,
                                                error = null
                                            )

                                        } ?: run {
                                            _state.value = _state.value.copy(
                                                showResponseAlert = true,
                                                shouldCaptureAnswer = false,
                                                shouldCaptureWorkArea = false,
                                                response = null,
                                                error = "No data in vision recognition response, try again"
                                            )
                                        }
                                    }
                            } catch (e: Exception) {
                                _state.value = _state.value.copy(
                                    showResponseAlert = true,
                                    shouldCaptureAnswer = false,
                                    shouldCaptureWorkArea = false,
                                    response = null,
                                    error = e.message ?: "Error processing the answer image"
                                )
                            }

                        }
                        ResultStatus.ERROR -> {
                            println("Firebase storage error: ${response.message}")
                        }
                    }
                }

            }
            */

            is NumeracyAssessmentAction.OnCountMatchAnswerChange -> {
                _state.value = _state.value.copy(
                    countMatchAnswer = action.countMatchAnswer
                )

                println("Count match answer updated: ${_state.value.countMatchAnswer}")
            }

            /*
            is NumeracyAssessmentAction.OnAnswerImageBitmapChange -> {
                _state.update { it.copy(answerImageBitmap = action.imageBitmap) }
            }*/
            is NumeracyAssessmentAction.OnAnswerImageFilePathChange -> {
                _state.update { it.copy(answerFilePath = action.path) }
            }
            /*
            is NumeracyAssessmentAction.OnWorkAreaImageBitmapChange -> {
                _state.update { it.copy(workAreaImageBitmap = action.imageBitmap) }
            }

             */
            is NumeracyAssessmentAction.OnWorkAreaImageFilePathChange -> {
                _state.update { it.copy(workAreaFilePath = action.path) }
            }
        }
    }

    private val _answerImageByteArrayState = MutableStateFlow<Results<VisionRecognition>>(Results.initial())
    val answerImageByteArrayState = _answerImageByteArrayState.asStateFlow()

    private val _workAreaImageByteArrayState = MutableStateFlow<Results<VisionRecognition>>(Results.initial())
    val workAreaImageByteArrayState = _workAreaImageByteArrayState.asStateFlow()

    /*
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

     */





    private fun submitCountAndMatch(
        assessmentId: String,
        studentId: String,
        onSuccess: () -> Unit
    ) {

        _state.update { it.copy(isLoading = true) }

        if (_state.value.numeracyLevel != NumeracyAssessmentLevel.COUNT_MATCH){
            return
        }

        val countMatchList = _state.value.numeracyAssessmentContent?.countAndMatchNumbersList ?: emptyList()

        if (countMatchList.isEmpty()){
            return
        }



        when{
            _state.value.currentIndex == countMatchList.size - 1 -> {
                _state.update {
                    it.copy(
                        countAndMatchResults = _state.value.countAndMatchResults.apply { add(
                            CountMatch(
                                expected_number = countMatchList[it.currentIndex],
                                student_count = _state.value.countMatchAnswer ?: 0,
                                passed = checkAnswer(
                                    answer = _state.value.countMatchAnswer ?: 0,
                                    correctAnswer = countMatchList[it.currentIndex]
                                )
                            )
                        )}
                    )
                }

                viewModelScope.launch {
                    localDataSource.insertPendingCountMatch(
                        assessmentId = assessmentId,
                        studentId = studentId,
                        countList = _state.value.countAndMatchResults.toList(),

                    )
                }

                _state.update { it.copy(
                    currentIndex = 0,
                    countMatchAnswer = null,
                    isLoading = false,
                    numeracyLevel = NumeracyAssessmentLevel.ADDITION,
                ) }
            }
            else -> {
                _state.update {
                    it.copy(
                        countAndMatchResults = _state.value.countAndMatchResults.apply { add(
                            CountMatch(
                                expected_number = countMatchList[it.currentIndex],
                                student_count = _state.value.countMatchAnswer ?: 0,
                                passed = checkAnswer(
                                    answer = _state.value.countMatchAnswer ?: 0,
                                    correctAnswer = countMatchList[it.currentIndex]
                                )
                            )
                        )},
                    )
                }

                _state.update { it.copy(
                    currentIndex = it.currentIndex + 1,
                    countMatchAnswer = null,
                    isLoading = false,
                ) }

            }
        }
    }

    private fun submitNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String,
        onSuccess: () -> Unit
    ) {
        val arithmeticContent = when (_state.value.numeracyLevel) {
            NumeracyAssessmentLevel.ADDITION -> _state.value.numeracyAssessmentContent?.additions
            NumeracyAssessmentLevel.SUBTRACTION -> _state.value.numeracyAssessmentContent?.subtractions
            NumeracyAssessmentLevel.MULTIPLICATION -> _state.value.numeracyAssessmentContent?.multiplications
            NumeracyAssessmentLevel.DIVISION -> _state.value.numeracyAssessmentContent?.divisions
            else -> null
        } ?: return

        val currentIndex = _state.value.currentIndex
        if (currentIndex !in arithmeticContent.indices) return

        fun getNextLevel(level: NumeracyAssessmentLevel): NumeracyAssessmentLevel? = when (level) {
            NumeracyAssessmentLevel.ADDITION -> NumeracyAssessmentLevel.SUBTRACTION
            NumeracyAssessmentLevel.SUBTRACTION -> NumeracyAssessmentLevel.MULTIPLICATION
            NumeracyAssessmentLevel.MULTIPLICATION -> NumeracyAssessmentLevel.DIVISION
            NumeracyAssessmentLevel.DIVISION -> NumeracyAssessmentLevel.WORD_PROBLEM
            else -> null
        }

        val currentOperation = arithmeticContent[currentIndex]

        evaluateNumeracyArithmeticOperationWithWorkManager(
            assessmentId = assessmentId,
            studentId = studentId,
            operationType = currentOperation.operationType.name,
            operationNumber1 = currentOperation.firstNumber,
            operationNumber2 = currentOperation.secondNumber,
            expectAnswer = currentOperation.answer,
            answerImagePath = _state.value.answerFilePath,
            workoutImagePath = _state.value.workAreaFilePath,
            onFailure = {
                _state.update {
                    it.copy(
                        isLoading = false,
                        shouldCaptureAnswer = false,
                    )
                }
            },
            onSuccess = {
                _state.update { state ->
                    if (currentIndex >= arithmeticContent.size - 1) {
                        state.copy(
                            currentIndex = 0,
                            answerFilePath = null,
                            shouldCaptureAnswer = false,
                            workAreaFilePath = null,
                            isLoading = false,
                            numeracyLevel = getNextLevel(state.numeracyLevel) ?: state.numeracyLevel,
                        )
                    } else {
                        state.copy(
                            shouldCaptureAnswer = false,
                            currentIndex = state.currentIndex + 1,
                            answerFilePath = null,
                            workAreaFilePath = null,
                            isLoading = false,
                        )
                    }
                }
                onSuccess()
            }
        )
    }


    private fun evaluateNumeracyArithmeticOperationWithWorkManager(
        assessmentId: String?,
        studentId: String?,
        operationType: String,
        operationNumber1: Int,
        operationNumber2: Int,
        expectAnswer: Int,
        answerImagePath: String?,
        workoutImagePath: String?,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty() || answerImagePath.isNullOrEmpty()){
                println("Assessment ID, Student ID, or Answer Image Path is null or empty.")

                println("assessmentId: $assessmentId, studentId: $studentId, answerImagePath: $answerImagePath")
                onFailure.invoke()
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val workData = workDataOf(
                EvaluateNumeracyArithmeticOperationWorker.ASSESSMENT_ID to assessmentId,
                EvaluateNumeracyArithmeticOperationWorker.STUDENT_ID to studentId,
                EvaluateNumeracyArithmeticOperationWorker.OPERATION_TYPE to operationType,
                EvaluateNumeracyArithmeticOperationWorker.OPERATION_NUMBER1 to operationNumber1,
                EvaluateNumeracyArithmeticOperationWorker.OPERATION_NUMBER2 to operationNumber2,
                EvaluateNumeracyArithmeticOperationWorker.EXPECTED_ANSWER to expectAnswer,
                EvaluateNumeracyArithmeticOperationWorker.ANSWER_IMAGE_PATH to answerImagePath,
                EvaluateNumeracyArithmeticOperationWorker.WORKOUT_IMAGE_PATH to workoutImagePath,
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()

            val tag = "assessment_${assessmentId}_${studentId}"

            val request = OneTimeWorkRequestBuilder<EvaluateReadingAssessmentWorker>()
                .setInputData(workData)
                .setConstraints(constraints = constraints)
                .addTag(tag = tag)
                .build()

            val uniqueWorkName = "reading_assessment_${assessmentId}_${studentId}_${System.currentTimeMillis()}"

            workManager
                .enqueueUniqueWork(
                    uniqueWorkName = uniqueWorkName,
                    ExistingWorkPolicy.REPLACE,
                    request = request
                )

            delay(1000)

            _state.update { it.copy(isLoading = false)}
            onSuccess.invoke()
        }

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