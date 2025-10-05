package com.nyansapoai.teaching.presentation.attendances.collectAttendance

data class CollectAttendanceState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)