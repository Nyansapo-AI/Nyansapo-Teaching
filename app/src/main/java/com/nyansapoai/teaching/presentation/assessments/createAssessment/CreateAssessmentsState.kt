package com.nyansapoai.teaching.presentation.assessments.createAssessment

data class CreateAssessmentsState(
    val name: String = "",
    val type: String = "",
    val startLevel: String = "",
    val assessmentNumber: Int = 1,
    val assignedStudents: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)