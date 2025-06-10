package com.nyansapoai.teaching.presentation.assessments.createAssessment

import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent

sealed interface CreateAssessmentsAction {

    data class SetName(val name: String) : CreateAssessmentsAction

    data class SetType(val type: String) : CreateAssessmentsAction

    data class SetStartLevel(val startLevel: String) : CreateAssessmentsAction

    data class SetAssessmentNumber(val assessmentNumber: Int) : CreateAssessmentsAction

    data class ToggleTypeDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class ToggleStartLevelDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class ToggleAssessmentNumberDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class ToggleStudentListDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class AddAssignedStudent(val student: AssignedStudent) : CreateAssessmentsAction

    data class SubmitAssessment(val onSuccess: () -> Unit) : CreateAssessmentsAction

}