package com.nyansapoai.teaching.presentation.assessments

data class AssessmentsState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)