package com.nyansapoai.teaching.domain.models.assessments.numeracy

data class NumeracyArithmeticOperation(
    val type: String = "",
    val expected_answer: Int = 0,
    val student_answer: Int? = null,
    val operationNumber1: Int = 0,
    val operationNumber2: Int = 0,
    val metadata: NumeracyOperationMetadata? = null,
)


data class NumeracyOperationMetadata(
    val workAreaMediaUrl: String? = null,
    val answerMediaUrl: String? = null,
    val passed: Boolean? = null
)
