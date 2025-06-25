package com.nyansapoai.teaching.presentation.assessments.literacy

import com.nyansapoai.teaching.domain.models.assessments.literacy.LiteracyAssessmentData
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.literacyAssessmentContent
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel

data class LiteracyState(
    val currentIndex: Int = 0,
    val showInstructions: Boolean = true,
    val title: String = "",
    val showContent: Boolean = false,
    val audioByteArray: ByteArray? = null,

    val response: String? = null,
    val error: String? = null,
    val audioUrl: String? = null,

    val currentAssessmentLevel: LiteracyAssessmentLevel = LiteracyAssessmentLevel.LETTER_RECOGNITION,
    val assessmentContent: LiteracyAssessmentData? = literacyAssessmentContent[0],
    val readingAssessmentResults: MutableList<ReadingAssessmentResult> = mutableListOf(),
)