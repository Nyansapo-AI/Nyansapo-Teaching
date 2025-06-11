package com.nyansapoai.teaching.presentation.common.textToSpeech

data class TextToSpeechState(
    val text: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null,
    val selectedVoice: String = "en-US-JennyNeural"
)