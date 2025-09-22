package com.nyansapoai.teaching.presentation.assessments.numeracy

import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.presentation.assessments.literacy.LiteracyAction
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel

sealed interface NumeracyAssessmentAction {
    data class OnAnswerImageFilePathChange(val path: String) : NumeracyAssessmentAction
    data class OnWorkAreaImageFilePathChange(val path: String) : NumeracyAssessmentAction
    data class OnIsSubmittingChange(val isSubmitting: Boolean) : NumeracyAssessmentAction
    data class OnShowResponseAlertChange(val showResponseAlert: Boolean) : NumeracyAssessmentAction
    data class OnCountMatchAnswerChange(val countMatchAnswer: Int) : NumeracyAssessmentAction
    data class OnAddArithmeticOperation(val numeracyOperations: NumeracyOperations, val onSuccess: () -> Unit) : NumeracyAssessmentAction
    data class OnAddNumberRecognition(val numberRecognition: NumeracyNumberRecognition, val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitCountMatch(val countMatch: List<CountMatch>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitNumeracyOperations(val operationList: List<NumeracyArithmeticOperation>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitNumberRecognition(val numberRecognitionList: List<NumeracyNumberRecognition>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitWordProblem(val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnNumeracyLevelChange(val numeracyLevel: NumeracyAssessmentLevel): NumeracyAssessmentAction

    data class OnShowInstructionChange(val showInstruction: Boolean) : NumeracyAssessmentAction
    data class OnShowContentChange(val showContent: Boolean) : NumeracyAssessmentAction
    data class OnAudioFilePathChange(val audioFilePath: String) : NumeracyAssessmentAction

    data object OnSubmitAnswer : NumeracyAssessmentAction
    data object OnClearAnswer : NumeracyAssessmentAction
    data object OnClearWorkArea : NumeracyAssessmentAction

    data class SubmitNumeracyAssessmentResults(
        val assessmentId: String,
        val studentId: String,

    ) : NumeracyAssessmentAction
}