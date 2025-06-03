package com.nyansapoai.teaching.domain.models.assessments.numeracy

data class NumeracyWordProblem(
    val question: String = "",
    val expectedAnswer: Int = 0,
    val studentAnswer: Int? = null,
    val metadata: NumeracyOperationMetadata? = null
)
