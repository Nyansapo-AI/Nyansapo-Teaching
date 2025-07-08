package com.nyansapoai.teaching.domain.models.assessments.literacy

data class PendingMultipleChoicesResult(
    val id: Long,
    val studentId: String,
    val assessmentId: String,
    val multipleChoicesResult: MultipleChoicesResult
)
