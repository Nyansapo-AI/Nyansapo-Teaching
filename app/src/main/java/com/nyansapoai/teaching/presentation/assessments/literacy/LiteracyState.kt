package com.nyansapoai.teaching.presentation.assessments.literacy

import com.nyansapoai.teaching.domain.models.assessments.literacy.LiteracyAssessmentData
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.literacyAssessmentContent
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel

data class LiteracyState(
    val currentIndex: Int = 0,
    val showInstructions: Boolean = true,
    val title: String = "",
    val showContent: Boolean = false,
    val audioByteArray: ByteArray? = null,
    val audioFilePath: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null,

    val studentId: String? = null,
    val assessmentId: String? = null,

    val response: String? = null,
    val error: String? = null,
    val audioUrl: String? = null,

    val selectedChoice: String? = null,
    val options: List<String> = emptyList(),

    val currentAssessmentLevel: LiteracyAssessmentLevel = LiteracyAssessmentLevel.PRE_TEST,
    val currentAssessmentLevelIndex: Int = 0,
    val assessmentContent: LiteracyAssessmentData? = null,
    val readingAssessmentResults: MutableList<ReadingAssessmentResult> = mutableListOf(),
    val assessmentFlow: List<LiteracyAssessmentLevel> = listOf(
        LiteracyAssessmentLevel.LETTER_RECOGNITION,
        LiteracyAssessmentLevel.WORD,
        LiteracyAssessmentLevel.PARAGRAPH,
        LiteracyAssessmentLevel.STORY,
        LiteracyAssessmentLevel.MULTIPLE_CHOICE,
        LiteracyAssessmentLevel.COMPLETED
    ),
    val round: Int = 0,

    val multipleChoiceQuestionsResult: MutableList<MultipleChoicesResult> = mutableListOf(),

    val hasCompletedAssessment: Boolean = false,
    val showEndAssessmentDialog: Boolean = false,
)