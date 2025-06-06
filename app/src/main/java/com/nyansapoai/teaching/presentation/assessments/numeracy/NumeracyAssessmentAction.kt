package com.nyansapoai.teaching.presentation.assessments.numeracy

import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel

sealed interface NumeracyAssessmentAction {
    data class OnCaptureAnswer(val imageByteArray: ByteArray) : NumeracyAssessmentAction
    data class OnCaptureWorkArea(val imageByteArray: ByteArray) : NumeracyAssessmentAction
    data class OnShouldCaptureAnswerChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    data class OnShouldCaptureWorkAreaChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    data object OnSubmitAnswer : NumeracyAssessmentAction
    data object OnClearAnswer : NumeracyAssessmentAction
    data object OnClearWorkArea : NumeracyAssessmentAction


    data class OnAddCountMatch(val countMatch: CountMatch, val onSuccess: () -> Unit) : NumeracyAssessmentAction

    data class OnAddArithmeticOperation(val numeracyOperations: NumeracyOperations, val onSuccess: () -> Unit) : NumeracyAssessmentAction

    data class OnAddNumberRecognition(val numberRecognition: NumeracyNumberRecognition, val onSuccess: () -> Unit): NumeracyAssessmentAction

    data class OnSubmitCountMatch(val countMatchList: List<CountMatch>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction

    data class OnSubmitNumeracyOperations(val operationList: List<NumeracyArithmeticOperation>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction

    data class OnSubmitNumberRecognition(val numberRecognitionList: List<NumeracyNumberRecognition>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction

    data class OnSubmitWordProblem(val wordProblem: NumeracyWordProblem, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction

    data class OnCountMachIndexChange(val index: Int): NumeracyAssessmentAction

    data class OnAdditionIndexChange(val index: Int): NumeracyAssessmentAction

    data class OnSubtractionIndexChange(val index: Int): NumeracyAssessmentAction

    data class OnMultiplicationIndexChange(val index: Int): NumeracyAssessmentAction

    data class OnDivisionIndexChange(val index: Int): NumeracyAssessmentAction

    data class OnNumberRecognitionIndexChange(val index: Int): NumeracyAssessmentAction

    data class OnNumeracyLevelChange(val numeracyLevel: NumeracyAssessmentLevel): NumeracyAssessmentAction

}