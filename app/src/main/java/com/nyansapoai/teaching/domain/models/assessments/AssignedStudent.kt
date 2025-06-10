package com.nyansapoai.teaching.domain.models.assessments

data class AssignedStudent(
    val student_id: String = "",
    val student_name: String = "",
    val competence: Int? = null,
)
