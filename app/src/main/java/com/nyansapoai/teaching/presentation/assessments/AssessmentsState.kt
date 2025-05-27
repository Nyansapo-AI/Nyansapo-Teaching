package com.nyansapoai.teaching.presentation.assessments

import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.utils.Results

data class AssessmentsState(
    val assessmentListState: Results<List<Assessment>> = Results.initial(),
)