package com.nyansapoai.teaching.domain.models.assessments

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class RemoteCompletedAssessment(
    val assessmentId: String = "",
    val completed_assessment: Boolean = true,
    val student_id: String = "",
)
