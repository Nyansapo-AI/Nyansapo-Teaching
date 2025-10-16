package com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults

import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumberRecognitionResult
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.ImageResult

sealed interface NumeracyAssessmentResultAction {
    data class OnSelectedNumeracyRecognitionResult(val numberRecognition: NumberRecognitionResult?) : NumeracyAssessmentResultAction
    data class OnSelectImageResult(val imageResult: ImageResult?) : NumeracyAssessmentResultAction
}

