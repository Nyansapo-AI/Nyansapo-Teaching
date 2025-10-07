package com.nyansapoai.teaching.presentation.attendances.collectAttendance

sealed interface CollectAttendanceAction {
    data class OnMarkAttendance(val studentId: String, val isPresent: Boolean) : CollectAttendanceAction
    data class OnSelectedGrade(val grade: Int?) : CollectAttendanceAction
    data class OnSubmitAttendance(val onSuccess: () -> Unit) : CollectAttendanceAction
}