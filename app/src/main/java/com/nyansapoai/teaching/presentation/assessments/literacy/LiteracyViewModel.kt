package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentMetadata
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.literacyAssessmentContent
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.literacy.components.compareResponseStrings
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.get
import kotlin.text.compareTo

class LiteracyViewModel(
    private val assessmentRepository: AssessmentRepository,
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository,
    private val mediaRepository: MediaRepository
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

            /*
            val response = assessmentRepository.getLiteracyAssessmentContent()

            when (response.status) {
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    response.data?.let { data ->
                        _state.update {
                            it.copy(
                                assessmentContent = data,
                                isLoading = false,
                                error = null
                            )
                        }
                    } ?: run {
                        _state.update { it.copy(error = "No assessment content available.", isLoading = false) }
                    }
                }
                ResultStatus.ERROR -> {
                    _state.update { it.copy(error = response.message ?: "Error fetching assessment content", isLoading = false) }
                }
            }*/
        }
    }

    fun onAction(action: LiteracyAction) {
        when (action) {
            is LiteracyAction.OnSubmitResponse -> {
                onSubmitReadingAssessment(
                    assessmentId = action.assessmentId,
                    studentId = action.studentId
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
                onSubmitStoryAssessment(
                    studentId = action.studentId,
                    assessmentId = action.assessmentId
                )
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
        }
    }

    private fun recognizeAudio(audioByteArray: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            artificialIntelligenceRepository.getTextFromAudio(audioByteArray = audioByteArray)
                .catch { error ->
                    Log.e("Audio response", "audio response failed: ${error.message}")
                    _state.update {
                        it.copy(
                            error = error.message ?: "Unknown Error. Try Again",
                        )
                    }
                }
                .collect { response ->
                    Log.d("Audio response", "audio response successful: ${response.data}")

                    response.data?.let { data ->
                        _state.update {
                            it.copy(
                                error = "No response from AI"
                            )
                        }

                        if (data.DisplayText.isEmpty()){
                            return@collect
                        }

                        _state.update {
                            it.copy(
                                response = data.DisplayText,
                                audioByteArray = audioByteArray
                            )
                        }

                    } ?: run {
                        _state.update {
                            it.copy(
                                error = "No response from AI"
                            )
                        }

                    }

                }
        }
    }

    private fun saveAudio(audioByteArray: ByteArray){
        viewModelScope.launch(Dispatchers.IO) {
            val response = mediaRepository.saveAudio(audioByteArray = audioByteArray)

            when(response.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    response.data?.let{ data ->
                        if (data.isEmpty()){
                            return@launch
                        }

                        _state.update {
                            it.copy(audioUrl = data)
                        }
                    }

                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(
                            error = response.message ?: "Unknown Error"
                        )
                    }
                }
            }
        }
    }

    private fun addToReadingAssessmentResults(
        content: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {
            val assessmentType: String = _state.value.currentAssessmentLevel.label

            // Check if audio exists
            if (_state.value.audioByteArray == null) {
                _state.update { it.copy(error = "Please record your response before submitting.") }
                delay(4000)
                _state.update { it.copy(error = null) }

                return@launch

            }

            // Set loading state
            _state.update { it.copy(isLoading = true) }

            try {
                // Step 1: Recognize audio (wait for completion)
                _state.value.audioByteArray?.let { audio ->
                    // Call recognizeAudio but wait for it to complete
                    artificialIntelligenceRepository.getTextFromAudio(audioByteArray = audio)
                        .catch { error ->
                            Log.e("Audio response", "audio response failed: ${error.message}")
                            _state.update {
                                it.copy(
                                    error = error.message ?: "Unknown Error",
                                    isLoading = false,
                                    audioByteArray = null
                                )
                            }

                            delay(4000)
                            _state.update { it.copy(error = null) }
                        }
                        .collect { response ->
                            response.data?.let { data ->
                                if (data.DisplayText.isNotEmpty()) {
                                    _state.update { it.copy(response = data.DisplayText) }
                                } else {

                                    /**
                                     * resets the state if the response is empty, the student to retry
                                     */
                                    _state.update {
                                        it.copy(
                                            error = "Your answer was not received. Try Again",
                                            audioByteArray = null,
                                            isLoading = false
                                        )
                                    }

                                    delay(4000)
                                    _state.update { it.copy(error = null) }

                                    return@collect
                                }
                            }
                        }

                    _state.value.response?.let {
                        val saveResponse = mediaRepository.saveAudio(audioByteArray = audio)
                        if (saveResponse.status == ResultStatus.SUCCESS) {
                            saveResponse.data?.let { url ->
                                _state.update { it.copy(audioUrl = url) }
                            }
                        }
                    } ?: run {
                        _state.update { it.copy(
                            error = "No answer recorded. Please Try Again",
                            isLoading = false,
                            audioByteArray = null
                        ) }

                        delay(4000)
                        _state.update { it.copy(error = null) }

                        return@launch
                    }

                    _state.update { currentState ->

                        val comparisonResult = compareResponseStrings(content, _state.value.response ?: "")

                        val newResult = ReadingAssessmentResult(
                            type = assessmentType,
                            content = content,
                            metadata = ReadingAssessmentMetadata(
                                audio_url = _state.value.audioUrl ?: "",
                                transcript = _state.value.response ?: "",
                                passed = comparisonResult.isMatch
                            )
                        )

                        currentState.copy(
                            readingAssessmentResults = _state.value.readingAssessmentResults.apply { add(element = newResult) },
                            isLoading = false,
                            error = null
                        )

                    }

                    onSuccess.invoke()
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = "Error processing assessment: ${e.message}",
                    isLoading = false
                ) }

                delay(4000)
                _state.update { it.copy(error = null) }
            }

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

    fun submitReadingAssessment(
        assessmentId: String,
        studentId: String,
        readingAssessmentResults: List<ReadingAssessmentResult>,
        onSuccess: () -> Unit
    ){

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            val response = assessmentRepository.assessReadingAssessment(
                assessmentId = assessmentId,
                studentID = studentId,
                readingAssessmentResults = readingAssessmentResults
            )

            when(response.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    _state.update {
                        it.copy(
                            message = "Assessment submitted successfully.",
                            isLoading = false,
                            showInstructions = false,
                            showContent = false,
                            currentIndex = 0,
                            audioByteArray = null,
                            response = null,
                            audioUrl = null,
                            readingAssessmentResults = mutableListOf()
                        )
                    }

                    onSuccess.invoke()
                }
                ResultStatus.ERROR -> {

                    _state.update {
                        it.copy(
                            error = response.message ?: "Can not submit assessment result",
                            isLoading = false
                        )
                    }
                }
            }
        }

    }


    private fun submitMultipleChoiceQuestions(
        assessmentId: String,
        studentId: String,
        multipleChoiceQuestionsResult: List<MultipleChoicesResult>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            val response = assessmentRepository.assessMultipleChoiceQuestions(
                assessmentId = assessmentId,
                studentID = studentId,
                multipleChoiceQuestions = multipleChoiceQuestionsResult
            )

            when(response.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {
                    _state.update {
                        it.copy(
                            message = "Assessment submitted successfully.",
                            isLoading = false,
                            showInstructions = false,
                            showContent = false,
                            currentIndex = 0,
                            audioByteArray = null,
                            response = null,
                            audioUrl = null,
                            multipleChoiceQuestionsResult = mutableListOf()
                        )
                    }

                    onSuccess.invoke()
                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(
                            error = response.message ?: "Can not submit assessment result",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onSubmitReadingAssessment(
        assessmentId: String,
        studentId: String,
    ){

        val currentAssessmentContentList =when(_state.value.currentAssessmentLevel){
            LiteracyAssessmentLevel.LETTER_RECOGNITION -> _state.value.assessmentContent?.letters ?: emptyList()
            LiteracyAssessmentLevel.WORD -> _state.value.assessmentContent?.words ?: emptyList()
            LiteracyAssessmentLevel.PARAGRAPH -> _state.value.assessmentContent?.paragraphs
            LiteracyAssessmentLevel.STORY -> _state.value.assessmentContent?.storys[0]?.split(".") ?: emptyList()
            LiteracyAssessmentLevel.MULTIPLE_CHOICE -> emptyList()
            LiteracyAssessmentLevel.COMPLETED -> emptyList()
        }

        currentAssessmentContentList?.let {
            when{

                _state.value.currentIndex == currentAssessmentContentList.size - 1 -> {
                    addToReadingAssessmentResults(
                        content = currentAssessmentContentList[_state.value.currentIndex],
                        onSuccess = {
                            submitReadingAssessment(
                                assessmentId = assessmentId,
                                studentId = studentId,
                                readingAssessmentResults = _state.value.readingAssessmentResults,
                                onSuccess = {
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
                                            currentIndex = 0
                                        )
                                    }
                                }
                            )
                        }
                    )

                }

                else -> {

                    addToReadingAssessmentResults(
                        content = currentAssessmentContentList[_state.value.currentIndex],
                        onSuccess = {
                            _state.update {
                                it.copy(
                                    currentIndex = it.currentIndex + 1,
                                    showInstructions = true,
                                    showContent = false,
                                    audioByteArray = null,
                                    response = null,
                                    audioUrl = null
                                )
                            }
                        }
                    )

                }

            }
        } ?: run {
            _state.update {
                it.copy(
                    error = "No assessment content available."
                )
            }
        }
    }

    fun addMultipleChoiceResponse(
        correctOptions: List<String>,
        question: String,
        onSuccess: () -> Unit
    ) {
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

            try {
                _state.update {
                    val new = MultipleChoicesResult(
                        question = question,
                        options = _state.value.options,
                        student_answer = _state.value.selectedChoice,
                        passed = _state.value.selectedChoice in correctOptions
                    )

                    it.copy(
                        multipleChoiceQuestionsResult = _state.value.multipleChoiceQuestionsResult.apply { add(element = new) },
                        isLoading = false,
                        error = null
                    )

                }

                onSuccess.invoke()

            }catch (e: Exception){
                _state.update {
                    it.copy(
                        error = "Error processing multiple choice response: ${e.message}",
                        isLoading = false
                    )
                }

                delay(4000)
                _state.update { it.copy(error = null) }
            }
        }
    }


    fun onSubmitStoryAssessment(
        assessmentId: String,
        studentId: String,
    ) {

        val contentList = when(_state.value.currentAssessmentLevel){
            LiteracyAssessmentLevel.MULTIPLE_CHOICE -> _state.value.assessmentContent?.questionsData ?: emptyList()
            else -> emptyList()
        }

        when{
            contentList.isEmpty() -> {
                _state.update {
                    it.copy(error = "Something went wrong. Try again")
                    return
                }
            }

            _state.value.currentIndex == contentList.size - 1 -> {
                addMultipleChoiceResponse(
                    correctOptions = contentList[_state.value.currentIndex].multipleChoices.correctChoices,
                    question = contentList[_state.value.currentIndex].question,
                    onSuccess ={

                        submitMultipleChoiceQuestions(
                            assessmentId = assessmentId,
                            studentId = studentId,
                            multipleChoiceQuestionsResult = _state.value.multipleChoiceQuestionsResult,
                            onSuccess = {
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
                            }
                        )
                    }
                )


            }

            else -> {
                addMultipleChoiceResponse(
                    correctOptions = contentList[_state.value.currentIndex].multipleChoices.correctChoices,
                    question = contentList[_state.value.currentIndex].question,
                    onSuccess ={
                        _state.update {
                            it.copy(
                                currentIndex = it.currentIndex + 1,
                                options = emptyList(),
                                selectedChoice = null
                            )
                        }
                    }
                )
            }
        }

    }

}