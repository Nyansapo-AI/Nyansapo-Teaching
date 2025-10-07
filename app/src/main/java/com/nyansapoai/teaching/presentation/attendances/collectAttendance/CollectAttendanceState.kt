package com.nyansapoai.teaching.presentation.attendances.collectAttendance

import com.nyansapoai.teaching.domain.models.attendance.StudentAttendance
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent

data class CollectAttendanceState(
    val studentList: List<NyansapoStudent> = emptyList(),
    val studentAttendanceList: MutableList<StudentAttendance>  = studentList.map { student ->
        StudentAttendance(
                id = student.id,
                name = student.name,
                grade = student.grade,
                attendance = false
        )
    }.toMutableList(),
    val selectedGrade: Int? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
)