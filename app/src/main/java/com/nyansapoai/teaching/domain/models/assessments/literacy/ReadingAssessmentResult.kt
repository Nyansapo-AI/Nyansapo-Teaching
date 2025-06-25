package com.nyansapoai.teaching.domain.models.assessments.literacy

data class ReadingAssessmentResult(
    val type: String  = "",
    val content: String = "",
    val metadata: ReadingAssessmentMetadata? = null,
)
