package com.nyansapoai.teaching.domain.models.assessments

data class CompletedAssessment(
    val assessmentId: String,
    val studentId: String,
    val isCompleted: Boolean
)
