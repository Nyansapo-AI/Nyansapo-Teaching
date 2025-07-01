package com.nyansapoai.teaching.domain.models.assessments.literacy

data class MultipleChoicesResult(
    val question: String = "",
    val options: List<String> = emptyList(),
    val student_answer: String? = null,
    val passed: Boolean = false,
)
