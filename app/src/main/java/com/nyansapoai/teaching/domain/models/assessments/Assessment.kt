package com.nyansapoai.teaching.domain.models.assessments

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Assessment(
    val id: String = "",
    val name: String = "",
    val created_at: String = "",
    val type: String = "",
    val start_level: String = "",
    val assessmentNumber: Int = 0,
    val school_id: String = "",
    val level_distribution: List<AssessmentLevelDistribution> = emptyList(),
    val assigned_students: List<AssignedStudentDto> = emptyList(),
    val organization_id: String = "",
    val project_id: String = ""
)


@IgnoreExtraProperties
data class AssignedStudentDto(
    val id: String = "",
    val baseline: String? = null,
    val grade: Int? = null,
    val isLinked: Boolean = false,
//    val createdAt: String = "",
    val group: String = "",
//    val lastUpdated: String = "",
    val name: String = "",
    val sex: String = "",
    val has_done: Boolean = false,
    val first_name: String = "",
    val last_name: String = "",
)
