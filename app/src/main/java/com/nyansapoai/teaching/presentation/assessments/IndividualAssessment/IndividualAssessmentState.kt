package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment

import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.utils.Results

data class IndividualAssessmentState(
    val assessmentState: Results<Assessment> = Results.initial(),
    val completedAssessments : List<CompletedAssessment> = emptyList(),
    val selectedGrade: Int? = null,
    val studentsList: List<NyansapoStudent> = emptyList(),
)