package com.nyansapoai.teaching.presentation.assessments.createAssessment

import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent

sealed interface CreateAssessmentsAction {

    data class SetName(val name: String) : CreateAssessmentsAction

    data class SetType(val type: String) : CreateAssessmentsAction

    data class SetStartLevel(val startLevel: String) : CreateAssessmentsAction

    data class SetAssessmentNumber(val assessmentNumber: Int) : CreateAssessmentsAction

    data class ToggleTypeDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class ToggleStartLevelDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class ToggleAssessmentNumberDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class ToggleStudentListDropDown(val isExpanded: Boolean) : CreateAssessmentsAction

    data class AddAssignedStudent(val student: NyansapoStudent) : CreateAssessmentsAction

    data class SubmitAssessment(val onSuccess: () -> Unit) : CreateAssessmentsAction


    data class SetSelectedGrade(val grade: Int?) : CreateAssessmentsAction

    data class OnFetchStudents(val organizationId: String, val projectId: String, val schoolId: String, val grade: Int? = null) : CreateAssessmentsAction

}