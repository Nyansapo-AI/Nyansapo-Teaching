package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment

import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.utils.Results

data class IndividualAssessmentState(
    val assessmentState: Results<Assessment> = Results.initial(),
)