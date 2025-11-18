package com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult

import com.nyansapoai.teaching.domain.models.assessments.literacy.LiteracyAssessmentResults
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.AudioResult
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.ImageResult

data class LiteracyResultState(
    val isLoading: Boolean = false,
    val results: LiteracyAssessmentResults? = null,
    val error: String? = null,
    val words: List<LiteracyAssessmentResults.LiteracyResultsData.ReadingResult> = emptyList(),
    val letters: List<LiteracyAssessmentResults.LiteracyResultsData.ReadingResult> = emptyList(),
    val paragraphs: List<LiteracyAssessmentResults.LiteracyResultsData.ReadingResult> = emptyList(),
    val stories: List<LiteracyAssessmentResults.LiteracyResultsData.ReadingResult> = emptyList(),
    val multipleChoiceQuestions: List<MultipleChoicesResult> = emptyList(),
    val selectedImageResult: ImageResult? = null,
    val selectedAudioResult: AudioResult? = null,
)