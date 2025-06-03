package com.nyansapoai.teaching.domain.models.assessments.numeracy

data class CountMatch(
    val expected_number: Int? = null,
    val expected_count: Int? = null,
    val passed: Boolean? = null,
)
