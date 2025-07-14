package com.nyansapoai.teaching.presentation.assessments.createAssessment

import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent

data class CreateAssessmentsState(
    val name: String = "",
    val type: String = "",
    val startLevel: String = "",
    val isManager: Boolean = false,
    val assessmentNumber: Int = 5,
    val error: String? = null,
    val localSchoolInfo: LocalSchoolInfo? = null,
    val assignedStudents: List<NyansapoStudent> = emptyList(),
    val selectedGrade: Int? = null,
    val isTypeDropDownExpanded: Boolean = false,
    val isStartLevelDropDownExpanded: Boolean = false,
    val isAssessmentNumberDropDownExpanded: Boolean = false,
    val isStudentListDropDownExpanded: Boolean = false,
    val studentList: List<NyansapoStudent> = emptyList(),
    val assessmentTypeList: List<String> = listOf(
        "Numeracy",
        "Literacy",
    ),

    val numeracyStartLevelList: List<String> = listOf(
        "Count and Match",
        "Addition",
        "Subtraction",
        "Multiplication",
        "Division",
        "Word Problems"
    ),

    val literacyStartLevelList: List<String> = listOf(
        "Letter Recognition",
        "Words",
        "Sentence",
        "Paragraph",
        "Story"
    ),

    val selectedStartingLevel : List<String> = emptyList(),
    val assessmentNumberList: List<Int> = listOf(5),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
){
    val canSubmit: Boolean get()  = name.isNotBlank() &&
            type.isNotBlank() &&
//            startLevel.isNotBlank() &&
            assessmentNumber > 0 &&
            assignedStudents.isNotEmpty()
}