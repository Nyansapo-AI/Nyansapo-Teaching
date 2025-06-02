package com.nyansapoai.teaching.presentation.assessments.numeracy

data class NumeracyAssessmentState(
    val shouldCaptureAnswer: Boolean = false,
    val shouldCaptureWorkArea: Boolean = false,
    val answerImageByteArray: ByteArray? = null,
    val workAreaImageByteArray: ByteArray? = null,

)