package com.nyansapoai.teaching.presentation.common.textToSpeech

data class TextToSpeechState(
    val text: String = "Hello World! This is a sample text for text-to-speech conversion.",
    val isSpeaking: Boolean = false,
    val error: String? = null,
    val selectedVoice: String = "en-US-JennyNeural"
)