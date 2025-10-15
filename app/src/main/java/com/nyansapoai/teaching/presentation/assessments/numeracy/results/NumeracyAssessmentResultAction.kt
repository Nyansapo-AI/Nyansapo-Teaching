package com.nyansapoai.teaching.presentation.assessments.numeracy.results

import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumberRecognitionResult

sealed interface NumeracyAssessmentResultAction {
    data class OnSelectScreenshotImage(val imageUrl: String?) : NumeracyAssessmentResultAction

    data class OnSelectedNumeracyRecognitionResult(val numberRecognition: NumberRecognitionResult?) : NumeracyAssessmentResultAction
}

