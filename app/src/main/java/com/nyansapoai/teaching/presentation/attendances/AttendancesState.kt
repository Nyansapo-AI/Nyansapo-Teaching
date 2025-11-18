package com.nyansapoai.teaching.presentation.attendances

import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import java.time.LocalDate

data class AttendancesState(
    val currentLocalDate: LocalDate? = LocalDate.now(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentWeekDay: String? = LocalDate.now().toString(),
    val attendanceRecord: AttendanceRecord? = null,
    val localSchoolInfo: LocalSchoolInfo? = null,
    val showDetailedAttendance: Boolean = false,
)