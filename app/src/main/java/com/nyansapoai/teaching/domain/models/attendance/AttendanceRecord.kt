package com.nyansapoai.teaching.domain.models.attendance

data class AttendanceRecord(
//    val createdAt: Long = System.currentTimeMillis(),
    val date: String = "",
    val students: List<StudentAttendance> = emptyList()
)
