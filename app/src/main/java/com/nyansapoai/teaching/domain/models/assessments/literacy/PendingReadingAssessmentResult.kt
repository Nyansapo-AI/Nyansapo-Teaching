package com.nyansapoai.teaching.domain.models.assessments.literacy

data class PendingReadingAssessmentResult(
    val assessmentId: String,
    val studentId: String,
    val readingAssessmentResult: ReadingAssessmentResult
)
