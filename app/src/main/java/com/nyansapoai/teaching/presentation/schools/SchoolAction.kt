package com.nyansapoai.teaching.presentation.schools

sealed interface SchoolAction {
    data class OnShowSchoolSelector(val show: Boolean): SchoolAction
}