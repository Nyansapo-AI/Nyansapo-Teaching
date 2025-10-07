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
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.presentation.assessments.components.checkAnswer
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.MarkNumeracyAssessmentAsCompleteWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.UploadNumeracyWordProblemImagesWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.UploadNumeracyArithmeticOperationImageWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.SubmitNumeracyCountAndMatchResultWorker
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.UploadNumeracyReadingAssessmentAudioWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class NumeracyAssessmentViewModel(
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
            is NumeracyAssessmentAction.OnIsSubmittingChange -> {
                _state.update {
                    it.copy(
                        shouldCaptureAnswer = action.isSubmitting
                    )
                }
            }
            is NumeracyAssessmentAction.OnSubmitCountMatch -> {
                submitCountAndMatch(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    onSuccess = action.onSuccess
                )
            }
            is NumeracyAssessmentAction.OnSubmitNumberRecognition -> {
                submitNumeracyRecognitionAssessment(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId,
                    onSuccess = {}
                )
            }
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
            is NumeracyAssessmentAction.OnCountMatchAnswerChange -> {
                _state.value = _state.value.copy(
                    countMatchAnswer = action.countMatchAnswer
                )
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
                    numeracyLevel = NumeracyAssessmentLevel.NUMBER_RECOGNITION,
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

        uploadNumeracyArithmeticOperationImageWithWorkManager(
            assessmentId = assessmentId,
            studentId = studentId,
            operationType = currentOperation.operationType.name,
            operationNumber1 = currentOperation.firstNumber,
            operationNumber2 = currentOperation.secondNumber,
            expectAnswer = currentOperation.answer,
            answerImagePath = _state.value.answerFilePath,
            workoutImagePath = _state.value.workAreaFilePath,
            round = _state.value.numeracyArithmeticOperationRound,
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
                            numeracyArithmeticOperationRound = state.numeracyArithmeticOperationRound + 1,
                            numeracyLevel = getNextLevel(state.numeracyLevel) ?: state.numeracyLevel,
                        )
                    } else {
                        state.copy(
                            shouldCaptureAnswer = false,
                            currentIndex = state.currentIndex + 1,
                            numeracyArithmeticOperationRound = state.numeracyArithmeticOperationRound + 1,
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

    private fun uploadNumberRecognitionAudioWorkManager(
        assessmentId: String?,
        studentId: String?,
        round: Int,
        content: String?,
        type: String?,
        audioFilePath: String?,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ){
        if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty() || content.isNullOrEmpty() || type.isNullOrEmpty() || audioFilePath.isNullOrEmpty()) {
            println("Assessment ID, Student ID, Content, Type, or Audio File Path is null or empty.")
            println("assessmentId: $assessmentId, studentId: $studentId, content: $content, type: $type, audioFilePath: $audioFilePath")
            onFailure.invoke()
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val workData = workDataOf(
                UploadNumeracyReadingAssessmentAudioWorker.ASSESSMENT_ID to assessmentId,
                UploadNumeracyReadingAssessmentAudioWorker.STUDENT_ID to studentId,
                UploadNumeracyReadingAssessmentAudioWorker.ROUND to round,
                UploadNumeracyReadingAssessmentAudioWorker.CONTENT to content,
                UploadNumeracyReadingAssessmentAudioWorker.TYPE to type,
                UploadNumeracyReadingAssessmentAudioWorker.AUDIO_FILE_PATH to audioFilePath,
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()

            val tag = "assessment_${assessmentId}_${studentId}_${content}_$type"

            val request = OneTimeWorkRequestBuilder<UploadNumeracyReadingAssessmentAudioWorker>()
                .setInputData(workData)
                .setConstraints(constraints = constraints)
                .addTag(tag = tag)
                .build()

            val uniqueWorkName = "numeracy_assessment_audio_${assessmentId}_${studentId}_${System.currentTimeMillis()}"

            workManager
                .enqueueUniqueWork(
                    uniqueWorkName = uniqueWorkName,
                    ExistingWorkPolicy.KEEP,
                    request = request
                )

            delay(1000)

            _state.update { it.copy(isLoading = false) }

            onSuccess.invoke()

        }

    }


    private fun submitNumeracyRecognitionAssessment(
        assessmentId: String,
        studentId: String,
        onSuccess: () -> Unit = {}
    ){

        if (_state.value.numeracyLevel != NumeracyAssessmentLevel.NUMBER_RECOGNITION){
            return
        }

        val numberRecognitions = _state.value.numeracyAssessmentContent?.numberRecognitionList ?: return

        val currentIndex = _state.value.currentIndex
        if (currentIndex !in numberRecognitions.indices ) return

        val currentNumberRecognition = numberRecognitions[currentIndex]

        uploadNumberRecognitionAudioWorkManager(
            assessmentId = assessmentId,
            studentId = studentId,
            round = currentIndex + 1,
            content = currentNumberRecognition.toString(),
            type = "numberRecognition",
            audioFilePath = _state.value.audioFilePath,
            onSuccess = {
                _state.update { state ->
                    if (currentIndex >= numberRecognitions.size - 1) {
                        state.copy(
                            currentIndex = 0,
                            answerFilePath = null,
                            shouldCaptureAnswer = false,
                            workAreaFilePath = null,
                            audioFilePath = null,
                            isLoading = false,
                            numeracyLevel = NumeracyAssessmentLevel.ADDITION,
                        )
                    } else {
                        state.copy(
                            shouldCaptureAnswer = false,
                            currentIndex = state.currentIndex + 1,
                            answerFilePath = null,
                            workAreaFilePath = null,
                            audioFilePath = null,
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


    private fun uploadNumeracyArithmeticOperationImageWithWorkManager(
        assessmentId: String?,
        studentId: String?,
        operationType: String,
        operationNumber1: Int,
        operationNumber2: Int,
        expectAnswer: Int,
        round: Int,
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
                UploadNumeracyArithmeticOperationImageWorker.ASSESSMENT_ID to assessmentId,
                UploadNumeracyArithmeticOperationImageWorker.STUDENT_ID to studentId,
                UploadNumeracyArithmeticOperationImageWorker.OPERATION_TYPE to operationType,
                UploadNumeracyArithmeticOperationImageWorker.OPERATION_NUMBER1 to operationNumber1,
                UploadNumeracyArithmeticOperationImageWorker.OPERATION_NUMBER2 to operationNumber2,
                UploadNumeracyArithmeticOperationImageWorker.EXPECTED_ANSWER to expectAnswer,
                UploadNumeracyArithmeticOperationImageWorker.ANSWER_IMAGE_FILE_PATH to answerImagePath,
                UploadNumeracyArithmeticOperationImageWorker.WORKOUT_IMAGE_FILE_PATH to workoutImagePath,
                UploadNumeracyArithmeticOperationImageWorker.ROUND to round ,
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()

            val tag = "assessment_${assessmentId}_${studentId}_${operationType}_${operationNumber1}_${operationNumber2}"

            val request = OneTimeWorkRequestBuilder<UploadNumeracyArithmeticOperationImageWorker>()
                .setInputData(workData)
                .setConstraints(constraints = constraints)
                .addTag(tag = tag)
                .build()

            val uniqueWorkName = "number_recognition_${assessmentId}_${studentId}_${System.currentTimeMillis()}"

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

        uploadNumeracyWordProblemImageWithWorkManager(
            assessmentId = assessmentId,
            studentId = studentId,
            question = currentWordProblem.problem,
            expectedAnswer = currentWordProblem.answer,
            answerImagePath = _state.value.answerFilePath,
            workoutImagePath = _state.value.workAreaFilePath,
            round = _state.value.currentIndex,
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


    private fun uploadNumeracyWordProblemImageWithWorkManager(
        assessmentId: String?,
        studentId: String?,
        question: String?,
        expectedAnswer: Int,
        answerImagePath: String?,
        workoutImagePath: String?,
        onSuccess: () -> Unit,
        round: Int,
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
                UploadNumeracyWordProblemImagesWorker.ASSESSMENT_ID to assessmentId,
                UploadNumeracyWordProblemImagesWorker.STUDENT_ID to studentId,
                UploadNumeracyWordProblemImagesWorker.QUESTION to question,
                UploadNumeracyWordProblemImagesWorker.ROUND to round,
                UploadNumeracyWordProblemImagesWorker.EXPECTED_ANSWER to expectedAnswer,
                UploadNumeracyWordProblemImagesWorker.ANSWER_IMAGE_PATH to answerImagePath,
                UploadNumeracyWordProblemImagesWorker.WORKOUT_IMAGE_PATH to workoutImagePath,

            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()

            val tag = "assessment_${assessmentId}_${studentId}_${question}_$expectedAnswer"

            val request = OneTimeWorkRequestBuilder<UploadNumeracyWordProblemImagesWorker>()
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


        val submitNumeracyCountMatchResults = OneTimeWorkRequestBuilder<SubmitNumeracyCountAndMatchResultWorker>()
            .setInputData(workData)
            .setConstraints(constraints = constraints)
            .build()

        val markAsCompleteWork = OneTimeWorkRequestBuilder<MarkNumeracyAssessmentAsCompleteWorker>()
            .setInputData(workData)
            .build()

        
        workManager
            .beginUniqueWork(
                uniqueWorkName = "submit_numeracy_assessment_${assessmentId}_${studentId}",
                existingWorkPolicy = ExistingWorkPolicy.REPLACE,
                request = submitNumeracyCountMatchResults,
            )
            .then(markAsCompleteWork)
            .enqueue()

    }
}