package com.nyansapoai.teaching.domain.models.attendance

data class StudentAttendance(
    val id: String = "",
    val name: String = "",
    val grade: Int? = null,
    val attendance: Boolean = false
)
