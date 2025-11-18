package com.nyansapoai.teaching.domain.models.assessments

data class AssessmentProgress(
    val assessmentId: String,
    val studentId: String,
    val assessmentLevel: String,
    val index: Int
)
