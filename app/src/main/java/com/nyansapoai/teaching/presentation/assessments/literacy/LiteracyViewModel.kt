package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
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
import com.nyansapoai.teaching.domain.models.assessments.literacy.literacyAssessmentContent
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.EvaluateMultipleChoiceQuestionWorker
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.EvaluateReadingAssessmentWorker
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.LiteracyAssessmentsMonitorWorker
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.MarkAssessmentDoneWorker
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.MarkLiteracyAssessmentWorker
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.SubmitMultipleChoiceResultsWorker
import com.nyansapoai.teaching.presentation.assessments.literacy.workers.SubmitReadingAssessmentWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LiteracyViewModel(
    private val localDataSource: LocalDataSource,
    private val workManager: WorkManager
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LiteracyState())
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
            initialValue = LiteracyState()
        )


    fun fetchAssessmentContent(assessmentNo: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            _state.update {
                it.copy(assessmentContent = literacyAssessmentContent[assessmentNo], isLoading = false)
            }
        }
    }

    fun onAction(action: LiteracyAction) {
        when (action) {
            is LiteracyAction.SetIds -> {
                _state.update {
                    it.copy(
                        assessmentId = action.assessmentId,
                        studentId = action.studentId
                    )
                }
            }

            is LiteracyAction.OnSubmitResponse -> {
                onSubmitReadingAssessment(
                )
            }
            is LiteracyAction.SetAudioByteArray -> {
                _state.update {
                    it.copy(
                        audioByteArray = action.audioByteArray
                    )
                }
            }
            is LiteracyAction.SetCurrentIndex -> {
                _state.update {
                    it.copy(
                        currentIndex = action.index
                    )
                }
            }
            is LiteracyAction.SetResponse -> {
                _state.update {
                    it.copy(
                        response = action.response
                    )
                }
            }
            is LiteracyAction.SetShowContent -> {
                _state.update {
                    it.copy(
                        showContent = action.showContent
                    )
                }
            }
            is LiteracyAction.SetShowInstructions -> {
                _state.update {
                    it.copy(
                        showInstructions = action.showInstructions
                    )
                }
            }

            is LiteracyAction.OnSubmitMultipleChoiceResponse -> {
                onSubmitStoryAssessment()
            }
            is LiteracyAction.SetSelectedChoice -> {
                _state.update {
                    it.copy(
                        selectedChoice = action.selectedChoice
                    )
                }
            }

            is LiteracyAction.SetMultipleQuestionOptions -> {
                _state.update {
                    it.copy(
                        options = action.options
                    )
                }
            }

            is LiteracyAction.SetAudioFilePath -> {
                _state.update {
                    it.copy(
                        audioFilePath = action.audioFilePath
                    )
                }
            }

            is LiteracyAction.OnSubmitLiteracyResults -> {
                submitLiteracyAssessment(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId
                )
            }

            LiteracyAction.OnCompletePreTest -> {
                _state.update { it.copy(currentAssessmentLevel = LiteracyAssessmentLevel.LETTER_RECOGNITION) }
            }
        }
    }

    private fun evaluateReadingAssessmentWithWorkManager(
        assessmentId: String?,
        studentId: String?,
        audioFilePath: String?,
        content: String,
        type: String,
        round: Int,
        onSuccess: () -> Unit
    ){

        viewModelScope.launch(Dispatchers.IO) {

            if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty() || audioFilePath.isNullOrEmpty()) {
                _state.update {
                    it.copy(
                        error = "Assessment can not be evaluated"
                    )
                }
                Log.d("LiteracyViewModel", "AssessmentId or StudentId or AudioFilePath is null or empty")
                Log.d("LiteracyViewModel", "assessmentId: $assessmentId, studentId: $studentId, audioFilePath: $audioFilePath")
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val contentHash = content.hashCode().toString()

            val workData = workDataOf(
                "audioFilePath" to audioFilePath,
                "content" to content,
                "type" to type,
                "assessment_id" to assessmentId,
                "student_id" to studentId,
                "round" to round
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

            val uniqueWorkName = "reading_assessment_${assessmentId}_${studentId}_${contentHash}_${System.currentTimeMillis()}"

            workManager
                .enqueueUniqueWork(
                    uniqueWorkName = uniqueWorkName,
                    ExistingWorkPolicy.REPLACE,
                    request = request
                )

            localDataSource.insertLiteracyAssessmentWorkerRequest(
                assessmentId = assessmentId,
                studentId = studentId,
                requestId = request.id.toString(),
                type = "reading_assessment"
            )

            delay(1000)

            _state.update {
                it.copy(
                    isLoading = false,
                )
            }

            onSuccess.invoke()
        }
    }

    private fun resetAssessmentState() {
        _state.update {
            it.copy(
                isLoading = false,
                showInstructions = false,
                showContent = false,
                currentIndex = 0,
                audioByteArray = null,
                response = null,
                audioUrl = null,
                error = null
            )
        }
    }

    fun onSubmitReadingAssessment(){
        val currentAssessmentContentList =when(_state.value.currentAssessmentLevel){
            LiteracyAssessmentLevel.LETTER_RECOGNITION -> _state.value.assessmentContent?.letters?.take(5) ?: emptyList()
            LiteracyAssessmentLevel.WORD -> _state.value.assessmentContent?.words?.take(5) ?: emptyList()
//            LiteracyAssessmentLevel.PARAGRAPH -> _state.value.assessmentContent?.paragraphs[0]?.split(".") ?: emptyList()
            LiteracyAssessmentLevel.PARAGRAPH -> listOf(_state.value.assessmentContent?.paragraphs?.get(0) ?: "")
//            LiteracyAssessmentLevel.STORY -> _state.value.assessmentContent?.storys[0]?.story?.trim()?.split(".") ?: emptyList()
            LiteracyAssessmentLevel.STORY -> listOf(_state.value.assessmentContent?.storys?.get(0)?.story ?: "")
            LiteracyAssessmentLevel.MULTIPLE_CHOICE -> emptyList()
            LiteracyAssessmentLevel.COMPLETED -> emptyList()
            LiteracyAssessmentLevel.PRE_TEST -> emptyList()
        }

        currentAssessmentContentList?.let {
            evaluateReadingAssessmentWithWorkManager(
                assessmentId = _state.value.assessmentId ?: return@let,
                studentId = _state.value.studentId ?: return@let,
                content = currentAssessmentContentList[_state.value.currentIndex],
                type = _state.value.currentAssessmentLevel.label,
                audioFilePath = _state.value.audioFilePath,
                round = _state.value.round,
                onSuccess = {
                    when{
                        _state.value.currentIndex == currentAssessmentContentList.size - 1 -> {

                            _state.update {
                                val nextIndex = if (_state.value.currentAssessmentLevelIndex < _state.value.assessmentFlow.size - 1)
                                    _state.value.currentAssessmentLevelIndex + 1
                                else 0

                                val nextLevel = if (nextIndex < _state.value.assessmentFlow.size)
                                    _state.value.assessmentFlow[nextIndex]
                                else
                                    _state.value.assessmentFlow[0]

                                it.copy(
                                    currentAssessmentLevelIndex = nextIndex,
                                    currentAssessmentLevel = nextLevel,
                                    showInstructions = true,
                                    currentIndex = 0,
                                    round = it.round +1,
                                    message = null,
                                    audioByteArray = null,
                                    audioFilePath = null,
                                )
                            }
                        }

                        else -> {
                            _state.update {
                                it.copy(
                                    currentIndex = it.currentIndex + 1,
                                    round = it.round +1,
                                    showContent = false,
                                    audioByteArray = null,
                                    audioFilePath = null,
                                    message = null,
                                    response = null,
                                    audioUrl = null
                                )
                            }
                        }
                    }

                }
            )
        } ?: run {
            _state.update {
                it.copy(
                    error = "No assessment content available."
                )
            }
        }
    }

    private fun evaluateMultipleChoicesAssessmentWithWorkManager(
        question: String,
        studentId: String?,
        assessmentId: String?,
        correctOptions: List<String>,
        onSuccess: () -> Unit
    ){

        if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty()) {
            _state.update {
                it.copy(
                    error = "Assessment can not be evaluated"
                )
            }
            return
        }



        viewModelScope.launch {
            if (_state.value.selectedChoice == null){
                _state.update {
                    it.copy(
                        error = "Please select an answer before submitting."
                    )
                }
                return@launch
            }

            if (_state.value.options.isEmpty()){
                _state.update {
                    it.copy(error = "No options available for this question." )
                }
                return@launch
            }


            _state.update { it.copy(isLoading = true) }

            val workData = workDataOf(
                "studentId" to studentId,
                "assessmentId" to assessmentId,
                "question" to question,
                "options" to _state.value.options.toList().toTypedArray(),
                "studentAnswer" to _state.value.selectedChoice ,
                "passed" to ( _state.value.selectedChoice in correctOptions )
            )


            val questionHash = question.hashCode().toString()
            val tag = "assessment_${assessmentId}_${studentId}"

            val request = OneTimeWorkRequestBuilder<EvaluateMultipleChoiceQuestionWorker>()
                .setInputData(workData)
                .addTag(tag)
                .build()

            val uniqueWorkName = "multiple_choices_${assessmentId}_${studentId}_${questionHash}_${System.currentTimeMillis()}"

            workManager
                .enqueueUniqueWork(
                    uniqueWorkName = uniqueWorkName,
                    ExistingWorkPolicy.REPLACE,
                    request = request
                )


            localDataSource.insertLiteracyAssessmentWorkerRequest(
                assessmentId = assessmentId,
                studentId = studentId,
                requestId = request.id.toString(),
                type = "multiple_choices"
            )

            _state.update {
                it.copy(
                    isLoading = false,

                )
            }

            onSuccess.invoke()
        }
    }



    fun onSubmitStoryAssessment() {

        val contentList = when(_state.value.currentAssessmentLevel){
            LiteracyAssessmentLevel.MULTIPLE_CHOICE -> _state.value.assessmentContent?.storys[0]?.questionsData ?: emptyList()
            else -> emptyList()
        }

        if (contentList.isEmpty()){
            _state.update {
                it.copy(error = "Something went wrong. Try again")
            }
            return
        }

        evaluateMultipleChoicesAssessmentWithWorkManager(
            correctOptions = contentList[_state.value.currentIndex].multipleChoices.correctChoices,
            studentId = _state.value.studentId,
            assessmentId = _state.value.assessmentId,
            question = contentList[_state.value.currentIndex].question,
            onSuccess = {
                when{
                    _state.value.currentIndex == contentList.size - 1 -> {
                        _state.update {
                            val nextIndex = if (_state.value.currentAssessmentLevelIndex < _state.value.assessmentFlow.size - 1)
                                _state.value.currentAssessmentLevelIndex + 1
                            else 0

                            val nextLevel = if (nextIndex < _state.value.assessmentFlow.size)
                                _state.value.assessmentFlow[nextIndex]
                            else
                                _state.value.assessmentFlow[0]



                            it.copy(
                                currentAssessmentLevelIndex = nextIndex,
                                currentAssessmentLevel = nextLevel,
                                currentIndex = 0,
                                multipleChoiceQuestionsResult = mutableListOf()
                            )
                        }

                        addCompleteAssessment()

                    }


                    else -> {
                        _state.update {
                            it.copy(
                                currentIndex = it.currentIndex + 1,
                                options = emptyList(),
                                selectedChoice = null
                            )
                        }
                    }
                }
            }
        )
    }


    private fun addCompleteAssessment(){
        if (_state.value.studentId.isNullOrBlank() ||_state.value.assessmentId.isNullOrBlank()){
            return
        }

        viewModelScope.launch {
            localDataSource.insertCompletedAssessment(
                studentId = _state.value.studentId ?: "",
                assessmentId = _state.value.assessmentId ?: ""
            )

        }
    }


    private fun submitLiteracyAssessment(
        assessmentId: String,
        studentId: String,
    ){

        val workData = workDataOf(
            "student_id" to studentId,
            "assessment_id" to assessmentId
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val submitMultipleChoicesResultsRequest = OneTimeWorkRequestBuilder<SubmitMultipleChoiceResultsWorker>()
            .setInputData(workData)
            .setConstraints(constraints = constraints)
            .build()

        val markAssessmentDoneRequest = OneTimeWorkRequestBuilder<MarkAssessmentDoneWorker>()
            .setInputData(workData)
            .build()

        workManager
            .beginUniqueWork(
                uniqueWorkName ="complete_assessment_${assessmentId}_${studentId}",
                 existingWorkPolicy =  ExistingWorkPolicy.REPLACE,
                request = markAssessmentDoneRequest
            )
            .then(submitMultipleChoicesResultsRequest)
            .enqueue()
    }

}