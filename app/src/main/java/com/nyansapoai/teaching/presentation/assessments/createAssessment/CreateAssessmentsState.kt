package com.nyansapoai.teaching.presentation.assessments.createAssessment

import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent

data class CreateAssessmentsState(
    val name: String = "",
    val type: String = "",
    val startLevel: String = "",
    val assessmentNumber: Int = 1,
    val assignedStudents: List<AssignedStudent> = emptyList(),
    val isTypeDropDownExpanded: Boolean = false,
    val isStartLevelDropDownExpanded: Boolean = false,
    val isAssessmentNumberDropDownExpanded: Boolean = false,
    val isStudentListDropDownExpanded: Boolean = false,
    val studentList: List<AssignedStudent> = listOf(
        AssignedStudent(
            student_id = "student_1",
            student_name = "John Doe",
            competence = null
        ),
        AssignedStudent(
            student_id = "student_2",
            student_name = "Jane Smith",
            competence = null
        ),
        AssignedStudent(
            student_id = "student_3",
            student_name = "Alice Johnson",
            competence = null
        ),
        AssignedStudent(
            student_id = "student_4",
            student_name = "Bob Brown",
            competence = null
        ),
        AssignedStudent(
            student_id = "student_5",
            student_name = "Charlie Davis",
            competence = null
        )
    ),
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
    val assessmentNumberList: List<Int> = listOf(1, 2, 3, 4, 5),
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