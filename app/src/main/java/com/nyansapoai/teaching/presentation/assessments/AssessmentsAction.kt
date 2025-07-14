package com.nyansapoai.teaching.presentation.assessments

sealed interface AssessmentsAction {
    data class OnGetCompletedAssessments(
        val assessmentId: String,
    ) : AssessmentsAction
}