package com.nyansapoai.teaching.presentation.assessments.numeracy.results

sealed interface NumeracyAssessmentResultAction {
    data class OnSelectScreenshotImage(val imageUrl: String?) : NumeracyAssessmentResultAction

    data class OnSelectAudioUrl(val audioUrl: String?) : NumeracyAssessmentResultAction
}