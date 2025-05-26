package com.nyansapoai.teaching.presentation.assessments.createAssessment

sealed interface CreateAssessmentsAction {

    data class SetName(val name: String) : CreateAssessmentsAction

    data class SetType(val type: String) : CreateAssessmentsAction

    data class SetStartLevel(val startLevel: String) : CreateAssessmentsAction

    data class SetAssessmentNumber(val assessmentNumber: Int) : CreateAssessmentsAction

    data class AddAssignedStudent(val studentId: String) : CreateAssessmentsAction

}