package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
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
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository
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

}