package com.nyansapoai.teaching.presentation.assessments.numeracy

sealed interface NumeracyAssessmentAction {
    data class OnCaptureAnswer(val imageByteArray: ByteArray) : NumeracyAssessmentAction
    data class OnCaptureWorkArea(val imageByteArray: ByteArray) : NumeracyAssessmentAction
    data class OnShouldCaptureAnswerChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    data class OnShouldCaptureWorkAreaChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    data object OnSubmitAnswer : NumeracyAssessmentAction
    data object OnClearAnswer : NumeracyAssessmentAction
    data object OnClearWorkArea : NumeracyAssessmentAction
}