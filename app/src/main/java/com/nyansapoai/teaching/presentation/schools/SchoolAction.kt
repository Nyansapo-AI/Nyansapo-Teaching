package com.nyansapoai.teaching.presentation.schools

sealed interface SchoolAction {
    data class OnShowSchoolSelector(val show: Boolean): SchoolAction
    data class OnSelectSchool(val organizationId: String, val projectId: String, val schoolId: String): SchoolAction

    data class OnFetchSchoolDetails(
        val organizationId: String,
        val projectId: String,
        val schoolId: String
    ): SchoolAction

    data object SignOut: SchoolAction
}