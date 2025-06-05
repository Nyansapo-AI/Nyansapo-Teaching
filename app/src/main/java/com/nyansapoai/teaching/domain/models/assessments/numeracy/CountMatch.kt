package com.nyansapoai.teaching.domain.models.assessments.numeracy

data class CountMatch(
    val type: String = "Count and Match",
    val expected_number: Int? = null,
    val student_count: Int? = null,
    val passed: Boolean? = null,
)
