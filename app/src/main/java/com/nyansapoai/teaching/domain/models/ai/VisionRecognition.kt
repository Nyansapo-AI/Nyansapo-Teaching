package com.nyansapoai.teaching.domain.models.ai

import kotlinx.serialization.Serializable

@Serializable
data class VisionRecognition(
    val url: String,
    val response: Int
)
