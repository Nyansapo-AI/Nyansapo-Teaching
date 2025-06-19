package com.nyansapoai.teaching.presentation.common.textToSpeech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.cognitiveservices.speech.ResultReason
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails
import com.microsoft.cognitiveservices.speech.SpeechSynthesisOutputFormat
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextToSpeechViewModel : ViewModel() {

    private val key1 = ""
    private val key2 = ""
    private val speechRegion = "eastus"

    private val _uiState = MutableStateFlow(TextToSpeechState())
    val uiState: StateFlow<TextToSpeechState> = _uiState.asStateFlow()

    private var speechSynthesizer: SpeechSynthesizer? = null

    init {
        initializeSpeechSynthesizer()
    }

    private fun initializeSpeechSynthesizer() {
        try {

            val speechConfig = SpeechConfig.fromSubscription(key2, speechRegion)
            speechConfig.speechSynthesisVoiceName = "en-US-JennyNeural"
            speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Audio16Khz32KBitRateMonoMp3)


            speechSynthesizer = SpeechSynthesizer(speechConfig)
        } catch (e: Exception) {

            println("Error initializing speech synthesizer: ${e.message}")

            _uiState.value = _uiState.value.copy(
                error = "Failed to initialize speech synthesizer: ${e.message}"
            )
        }
    }

    fun updateText(text: String) {
        _uiState.value = _uiState.value.copy(text = text, error = null)
    }

    fun updateVoice(voice: String) {
        _uiState.value = _uiState.value.copy(selectedVoice = voice)
        speechSynthesizer?.close()

        val speechConfig = SpeechConfig.fromSubscription(key2, speechRegion)
        speechConfig.speechSynthesisVoiceName = voice
        speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Audio16Khz32KBitRateMonoMp3)

        speechSynthesizer = SpeechSynthesizer(speechConfig)
    }

    fun speakText() {
        val currentText = _uiState.value.text
        if (currentText.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter some text to speak")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSpeaking = true, error = null)

            try {
                withContext(Dispatchers.IO) {
                    val result = speechSynthesizer?.SpeakText(currentText)

                    when (result?.reason) {
                        ResultReason.SynthesizingAudioCompleted -> {
                            // Success - audio was synthesized
                            println("Speech synthesis completed successfully")
                        }
                        ResultReason.Canceled -> {
                            println("Speech synthesis canceled")
                            val cancellation = SpeechSynthesisCancellationDetails.fromResult(result)
                            throw Exception("Speech synthesis canceled: ${cancellation.reason}")
                        }
                        else -> {
                            println("Speech synthesis failed with reason: ${result?.reason}")
                            throw Exception("Speech synthesis failed")
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Speech synthesis failed: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isSpeaking = false)
            }
        }
    }

    fun stopSpeaking() {
        speechSynthesizer?.StopSpeakingAsync()
        _uiState.value = _uiState.value.copy(isSpeaking = false)
    }

    override fun onCleared() {
        super.onCleared()
        speechSynthesizer?.close()
    }

}