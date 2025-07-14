package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment

sealed interface IndividualAssessmentAction {
    data class OnGetCompletedAssessments(
        val assessmentId: String,
    ) : IndividualAssessmentAction

    data class OnSetGrade(
        val grade: Int?
    ) : IndividualAssessmentAction
}