package com.nyansapoai.teaching.presentation.students

import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import kotlin.collections.List

data class StudentsState(
    val isLoading: Boolean = false,
    val isManager: Boolean = false,
    val studentList: List<NyansapoStudent> = emptyList(),
    val totalStudents : List<NyansapoStudent> = emptyList(),
    val selectedGrade: Int? = null,
    val selectedLevel: String? = null,
    val localSchoolInfo: LocalSchoolInfo? = null,
    val error: String? = null,

)