package com.nyansapoai.teaching.presentation.assessments

sealed interface AssessmentsAction {
    data class OnGetCompletedAssessments(
        val assessmentId: String,
    ) : AssessmentsAction

    data class FetchAssessments(
        val schoolId: String = "",
    ) : AssessmentsAction
}