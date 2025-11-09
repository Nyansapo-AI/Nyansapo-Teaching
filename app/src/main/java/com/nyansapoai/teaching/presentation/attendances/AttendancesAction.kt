package com.nyansapoai.teaching.presentation.attendances

import com.kizitonwose.calendar.core.WeekDay

sealed interface AttendancesAction {
    data class SetWeekDay(val weekDay: WeekDay) : AttendancesAction
    data class SetShowDetailedAttendance(val showDetailAttendance: Boolean) : AttendancesAction


}