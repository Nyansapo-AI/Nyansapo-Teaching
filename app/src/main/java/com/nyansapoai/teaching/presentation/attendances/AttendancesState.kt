package com.nyansapoai.teaching.presentation.attendances

import io.ktor.util.date.WeekDay
import java.time.LocalDate

data class AttendancesState(
    val paramOne: String = "default",
    val currentLocalDate: LocalDate? = LocalDate.now(),
    val currentWeekDay: String? = null,
    val paramTwo: List<String> = emptyList(),
)