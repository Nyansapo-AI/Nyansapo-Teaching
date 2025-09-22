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
import com.nyansapoai.teaching.presentation.assessments.components.checkAnswer
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyWordProblemWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.SubmitNumeracyArithmeticOperationResultsWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.SubmitNumeracyCountAndMatchResultWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.SubmitNumeracyWordProblemResultsWorker
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
            NumeracyAssessmentAction.OnClearAnswer -> {
                _state.value = _state.value.copy(
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

                submitNumeracyWordProblem(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    onSuccess = action.onSuccess
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
            is NumeracyAssessmentAction.OnCountMatchAnswerChange -> {
                _state.value = _state.value.copy(
                    countMatchAnswer = action.countMatchAnswer
                )

                println("Count match answer updated: ${_state.value.countMatchAnswer}")
            }
            is NumeracyAssessmentAction.OnAnswerImageFilePathChange -> {
                _state.update { it.copy(answerFilePath = action.path) }
            }
            is NumeracyAssessmentAction.OnWorkAreaImageFilePathChange -> {
                _state.update { it.copy(workAreaFilePath = action.path) }
            }
            is NumeracyAssessmentAction.SubmitNumeracyAssessmentResults -> {
                submitNumeracyAssessment(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                )
            }
            is NumeracyAssessmentAction.OnAudioFilePathChange -> {
                _state.update { it.copy(audioFilePath = action.audioFilePath) }
            }
            is NumeracyAssessmentAction.OnShowContentChange -> {
                _state.update { it.copy(showContent = action.showContent) }
            }
            is NumeracyAssessmentAction.OnShowInstructionChange -> {
                _state.update { it.copy(showInstruction = action.showInstruction) }
            }
        }
    }


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
            if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty() || answerImagePath.isNullOrEmpty() || workoutImagePath.isNullOrEmpty()) {
                println("Assessment ID, Student ID, or Answer Image Path is null or empty.")

                println("assessmentId: $assessmentId, studentId: $studentId, answerImagePath: $answerImagePath, workoutImagePath: $workoutImagePath")
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

            val request = OneTimeWorkRequestBuilder<EvaluateNumeracyArithmeticOperationWorker>()
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
        onSuccess: () -> Unit = {}
    ) {
        if (_state.value.numeracyLevel != NumeracyAssessmentLevel.WORD_PROBLEM){
            return
        }

        val wordproblems = _state.value.numeracyAssessmentContent?.wordProblems ?: return

        val currentIndex = _state.value.currentIndex
        if (currentIndex !in wordproblems.indices ) return

        val currentWordProblem = wordproblems[currentIndex]

        evaluateNumeracyWordProblemWithWorkManager(
            assessmentId = assessmentId,
            studentId = studentId,
            question = currentWordProblem.problem,
            expectedAnswer = currentWordProblem.answer,
            answerImagePath = _state.value.answerFilePath,
            workoutImagePath = _state.value.workAreaFilePath,
            onSuccess = {
                _state.update { state ->
                    if (currentIndex >= wordproblems.size - 1) {
                        state.copy(
                            currentIndex = 0,
                            answerFilePath = null,
                            shouldCaptureAnswer = false,
                            workAreaFilePath = null,
                            isLoading = false,
                            hasCompletedAssessment = true
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
            },
            onFailure = {
                _state.update {
                    it.copy(
                        isLoading = false,
                        shouldCaptureAnswer = false,
                    )
                }
            }
        )
    }


    private fun evaluateNumeracyWordProblemWithWorkManager(
        assessmentId: String?,
        studentId: String?,
        question: String?,
        expectedAnswer: Int,
        answerImagePath: String?,
        workoutImagePath: String?,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty() || answerImagePath.isNullOrEmpty() || workoutImagePath.isNullOrEmpty()) {
                println("Assessment ID, Student ID, or Answer Image Path is null or empty.")

                println("assessmentId: $assessmentId, studentId: $studentId, answerImagePath: $answerImagePath, workoutImagePath: $workoutImagePath")
                onFailure.invoke()
                return@launch

            }

            _state.update { it.copy(isLoading = true) }

            val workData = workDataOf(
                EvaluateNumeracyWordProblemWorker.ASSESSMENT_ID to assessmentId,
                EvaluateNumeracyWordProblemWorker.STUDENT_ID to studentId,
                EvaluateNumeracyWordProblemWorker.QUESTION to question,
                EvaluateNumeracyWordProblemWorker.EXPECTED_ANSWER to expectedAnswer,
                EvaluateNumeracyWordProblemWorker.ANSWER_IMAGE_PATH to answerImagePath,
                EvaluateNumeracyWordProblemWorker.WORKOUT_IMAGE_PATH to workoutImagePath,
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()

            val tag = "assessment_${assessmentId}_${studentId}"

            val request = OneTimeWorkRequestBuilder<EvaluateNumeracyWordProblemWorker>()
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

    private fun submitNumeracyAssessment(
        assessmentId: String,
        studentId: String,
    ){
        val workData = workDataOf(
            "assessment_id" to assessmentId,
            "student_id" to studentId,
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val submitNumeracyArithmeticOperationsResult = OneTimeWorkRequestBuilder<SubmitNumeracyArithmeticOperationResultsWorker>()
            .setInputData(workData)
            .setConstraints(constraints = constraints)
            .build()

        val submitNumeracyCountMatchResults = OneTimeWorkRequestBuilder<SubmitNumeracyCountAndMatchResultWorker>()
            .setInputData(workData)
            .setConstraints(constraints = constraints)
            .build()

        val submitNumeracyWordProblemResults = OneTimeWorkRequestBuilder<SubmitNumeracyWordProblemResultsWorker>()
            .setInputData(workData)
            .setConstraints(constraints = constraints)
            .build()
        
        workManager
            .beginUniqueWork(
                uniqueWorkName = "submit_numeracy_assessment_${assessmentId}_${studentId}",
                existingWorkPolicy = ExistingWorkPolicy.REPLACE,
                request = submitNumeracyCountMatchResults,
            )
            .then(submitNumeracyArithmeticOperationsResult)
            .then(submitNumeracyWordProblemResults)
            .enqueue()

    }
}