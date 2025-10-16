package com.nyansapoai.teaching.presentation.assessments.assessmentResult

data class AssessmentResultState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)

data class ImageResult(
    val studentId: String,
    val assessmentId: String,
    val imageUrl: String?,
    val studentAnswer: String,
    val expectedAnswer: String,
    val transcript: String,
    val passed: Boolean,
)

data class AudioResult(
    val studentId: String,
    val assessmentId: String,
    val audioUrl: String?,
    val studentAnswer: String,
    val expectedAnswer: String,
    val transcript: String,
    val passed: Boolean,
)