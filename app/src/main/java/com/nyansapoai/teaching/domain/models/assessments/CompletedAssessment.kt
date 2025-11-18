package com.nyansapoai.teaching.domain.models.assessments

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class CompletedAssessment(
    val assessmentId: String = "",
    val student_id: String = "",
    val completed_assessment: Boolean = true,
)
