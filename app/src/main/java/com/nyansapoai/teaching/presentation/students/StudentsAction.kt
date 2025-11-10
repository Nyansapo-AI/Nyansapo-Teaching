package com.nyansapoai.teaching.presentation.students

sealed interface StudentsAction {
    data class OnFetchStudents(
        val organizationId: String,
        val projectId: String,
        val schoolId: String,
        val grade: Int? = null,
    ) : StudentsAction

    data class OnSelectGrade(val grade: Int?) : StudentsAction
    data class OnSelectLevel(val level: String?) : StudentsAction
}