package com.nyansapoai.teaching.domain.models.assessments

data class Assessment(
    val id: String = "",
    val name: String = "",
    val created_at: String = "",
    val type: String = "",
    val start_level: String = "",
    val assessmentNumber: Int = 0,
    val level_distribution: List<AssessmentLevelDistribution> = emptyList(),
    val assigned_students: List<AssignedStudent> = emptyList()
)
