package com.nyansapoai.teaching.domain.models.assessments

data class Assessment(
    val id: String,
    val name: String,
    val created_at: String,
    val type: String,
    val start_level: String,
    val assessmentNumber: Int,
    val level_distribution: List<AssessmentLevelDistribution>,
    val assigned_students: List<AssignedStudent>
)
