package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentMetadata
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        }
    }


    private fun recognizeAudio(audioByteArray: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            artificialIntelligenceRepository.getTextFromAudio(audioByteArray = audioByteArray)
                .catch { error ->
                    Log.e("Audio response", "audio response failed: ${error.message}")
                    _state.update {
                        it.copy(
                            error = error.message ?: "Unknown Error"
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
    ) {

        val assessmentType: String = _state.value.currentAssessmentLevel.label

        if (_state.value.audioByteArray == null){
            _state.update {
                it.copy(
                    error = "Please record your response before submitting."
                )
            }
            return
        }

        _state.value.audioByteArray?.let {
            recognizeAudio(_state.value.audioByteArray!!)
        }?: run {
            _state.update {
                it.copy(
                    error = "No audio recorded."
                )
            }
            return
        }

        _state.value.response?.let {
            saveAudio(_state.value.audioByteArray!!)
        }?: run {
            _state.update {
                it.copy(
                    error = "No answer recorded."
                )
            }
            return
        }



        _state.update { currentState ->
            val newResult = ReadingAssessmentResult(
                type = assessmentType,
                content = content,
                metadata = ReadingAssessmentMetadata(
                    audio_url = _state.value.audioUrl ?: "",
                    transcript = _state.value.response ?: "",
                    passed = content == _state.value.response
                )
            )

            currentState.copy(readingAssessmentResults = _state.value.readingAssessmentResults.apply { add(element = newResult) } )
        }
    }


    fun submitReadingAssessment(
        assessmentId: String,
        studentId: String,
        readingAssessmentResults: List<ReadingAssessmentResult>
    ){

        viewModelScope.launch(Dispatchers.IO) {
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
                        it.copy(message = "Assessment submitted successfully.",
                            isLoading = false,
                            showInstructions = false,
                            showContent = false,
                            currentIndex = 0,
                            audioByteArray = null,
                            response = null,
                            audioUrl = null
                        )
                    }
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
        val assessmentFlow = listOf(
            LiteracyAssessmentLevel.LETTER_RECOGNITION,
            LiteracyAssessmentLevel.WORD,
            LiteracyAssessmentLevel.PARAGRAPH
        )

        val currentAssessmentContentList =when(_state.value.currentAssessmentLevel){
            LiteracyAssessmentLevel.LETTER_RECOGNITION -> _state.value.assessmentContent?.letters ?: emptyList()
            LiteracyAssessmentLevel.WORD -> _state.value.assessmentContent?.words ?: emptyList()
            LiteracyAssessmentLevel.PARAGRAPH -> _state.value.assessmentContent?.paragraphs
            LiteracyAssessmentLevel.STORY -> _state.value.assessmentContent?.storys
        }

        currentAssessmentContentList?.let {
            when{

                _state.value.currentIndex == currentAssessmentContentList.size - 1 -> {
                    // Last item, submit the assessment
                    addToReadingAssessmentResults(content = currentAssessmentContentList[_state.value.currentIndex])

                    submitReadingAssessment(
                        assessmentId = assessmentId,
                        studentId = studentId,
                        readingAssessmentResults = _state.value.readingAssessmentResults
                    )

                    _state.update {
                        it.copy(
                            currentAssessmentLevelIndex = if (_state.value.currentAssessmentLevelIndex < _state.value.assessmentFlow.size) _state.value.currentAssessmentLevelIndex + 1 else 0,
                            currentAssessmentLevel = _state.value.assessmentFlow[_state.value.currentAssessmentLevelIndex],
                        )
                    }

                }

                else -> {
                    addToReadingAssessmentResults(content = currentAssessmentContentList[_state.value.currentIndex])

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

            }
        } ?: run {
            _state.update {
                it.copy(
                    error = "No assessment content available."
                )
            }
        }


    }

}