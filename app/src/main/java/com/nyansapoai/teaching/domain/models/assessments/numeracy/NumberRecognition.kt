package com.nyansapoai.teaching.domain.models.assessments.numeracy

data class NumeracyNumberRecognition(
    val type: String = "",
    val content: Int = 0,
    val student_answer: Int? = null,
    val metadata: NumeracyOperationMetadata? = null,
)