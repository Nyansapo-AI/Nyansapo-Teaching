package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationMetadata
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

            is NumeracyAssessmentAction.OnAddAdditionOperation -> {
                _state.value = _state.value.copy(
                    additionOperationResults = _state.value.additionOperationResults.apply { add(action.addition) }
                )
            }

            is NumeracyAssessmentAction.OnAddCountMatch -> {
                _state.value = _state.value.copy(
                    countAndMatchResults = _state.value.countAndMatchResults.apply { add(action.countMatch) }
                )
            }
            is NumeracyAssessmentAction.OnAddDivisionOperation -> {
                _state.value = _state.value.copy(
                    divisionOperationResults = _state.value.divisionOperationResults.apply { add(action.division) }
                )
            }
            is NumeracyAssessmentAction.OnAddMultiplicationOperation -> {
                _state.value = _state.value.copy(
                    multiplicationOperationResults = _state.value.multiplicationOperationResults.apply { add(action.multiplication) }
                )
            }
            is NumeracyAssessmentAction.OnAddNumberRecognition -> {
                _state.value = _state.value.copy(
                    numberRecognitionResults = _state.value.numberRecognitionResults.apply { add(action.numberRecognition) }
                )
            }
            is NumeracyAssessmentAction.OnAddSubtractionOperation -> {
                _state.value = _state.value.copy(
                    subtractionOperationResults = _state.value.subtractionOperationResults.apply { add(action.subtraction) }
                )
            }
            is NumeracyAssessmentAction.OnSubmitCountMatch -> {
                submitCountAndMatch(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    countMatchList = _state.value.countAndMatchResults
                )
            }
            is NumeracyAssessmentAction.OnSubmitNumberRecognition -> TODO()
            is NumeracyAssessmentAction.OnSubmitNumeracyOperations -> {

            }
            is NumeracyAssessmentAction.OnSubmitWordProblem -> {
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
        countMatchList: List<CountMatch>
    ) {
        if (countMatchList.isEmpty()) {
            println("No count and match results to submit.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            assessmentRepository.assessNumeracyCountAndMatch(
                countAndMatchList = countMatchList,
                studentID = studentId,
                assessmentId = assessmentId
            )
        }
    }

    private fun submitNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String,
        operationList: List<NumeracyArithmeticOperation>
    ){
        if (operationList.isEmpty()){
            return
        }

        viewModelScope.launch {
            assessmentRepository.assessNumeracyArithmeticOperations(
                assessmentId = assessmentId,
                studentID = studentId,
                arithmeticOperations = operationList,
            )
        }
    }

/*
    init {
        submitNumeracyArithmeticOperations(
            assessmentId = " d0c525ba-b151-4f61-98dc-7920fe857e69 ",
            studentId = "student_2",
            operationList = listOf(
                NumeracyArithmeticOperation(
                    type = "Addition",
                    student_answer = 3,
                    expected_answer = 3,
                    operationNumber1 = 2,
                    operationNumber2 = 1,
                    metadata = NumeracyOperationMetadata(
                        workAreaMediaUrl = "https://example.com/work_area_1.jpg",
                        answerMediaUrl = "https://example.com/answer_1.jpg",
                        passed = true
                    )
                )
            )
        )


        submitCountAndMatch(
            assessmentId = "d0c525ba-b151-4f61-98dc-7920fe857e69",
            studentId = "student_2",
            countMatchList = listOf(
                CountMatch(
                    type = "Count and Match",
                    student_count = 5,
                    expected_number = 5,
                    passed = true,
                )
            )
        )
    }


 */
}