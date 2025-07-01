package com.nyansapoai.teaching.domain.models.ai

import kotlinx.serialization.Serializable

@Serializable
data class SpeechRecognition(
    val RecognitionStatus: String,
    val Offset: Int,
    val Duration: Int,
    val DisplayText: String
)
