package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
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
            is LiteracyAction.OnSubmitResponse -> {}
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
        type: String,
        content: String,
        transcript: String,
        audio_url: String
    ) {
        _state.update { currentState ->
            val newResult = ReadingAssessmentResult(
                type = type,
                content = content,
                metadata = ReadingAssessmentMetadata(
                    audio_url = audio_url,
                    transcript = transcript,
                    passed = content == transcript
                )
            )

            currentState.copy(readingAssessmentResults = _state.value.readingAssessmentResults.apply { add(element = newResult) } )
        }
    }




    fun onSubmitReadingAssessment(
        assessmentId: String,
        studentId: String,
    ){
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

    }

}