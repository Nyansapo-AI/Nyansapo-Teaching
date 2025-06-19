package com.nyansapoai.teaching.domain.dto.ai

import kotlinx.serialization.Serializable

@Serializable
data class GetTextFromImageRequestDTO(
    val url: String
)