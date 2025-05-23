package com.nyansapoai.teaching.presentation.assessments.createAssessment

data class CreateAssessmentsState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)