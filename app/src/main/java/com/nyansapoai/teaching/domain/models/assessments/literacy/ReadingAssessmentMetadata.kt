package com.nyansapoai.teaching.domain.models.assessments.literacy

data class ReadingAssessmentMetadata(
    val audio_url: String = "",
    val passed: Boolean = false,
    val transcript: String = "",
)
