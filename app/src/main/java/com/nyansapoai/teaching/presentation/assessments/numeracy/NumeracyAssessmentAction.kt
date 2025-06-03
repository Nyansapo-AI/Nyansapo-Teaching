package com.nyansapoai.teaching.presentation.assessments.numeracy

import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem

sealed interface NumeracyAssessmentAction {
    data class OnCaptureAnswer(val imageByteArray: ByteArray) : NumeracyAssessmentAction
    data class OnCaptureWorkArea(val imageByteArray: ByteArray) : NumeracyAssessmentAction
    data class OnShouldCaptureAnswerChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    data class OnShouldCaptureWorkAreaChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    data object OnSubmitAnswer : NumeracyAssessmentAction
    data object OnClearAnswer : NumeracyAssessmentAction
    data object OnClearWorkArea : NumeracyAssessmentAction


    data class OnAddCountMatch(val countMatch: CountMatch) : NumeracyAssessmentAction

    data class OnAddAdditionOperation(val addition: NumeracyArithmeticOperation) : NumeracyAssessmentAction

    data class OnAddSubtractionOperation(val subtraction: NumeracyArithmeticOperation) : NumeracyAssessmentAction

    data class OnAddMultiplicationOperation(val multiplication: NumeracyArithmeticOperation) : NumeracyAssessmentAction

    data class OnAddDivisionOperation(val division: NumeracyArithmeticOperation) : NumeracyAssessmentAction

    data class OnAddNumberRecognition(val numberRecognition: NumeracyNumberRecognition): NumeracyAssessmentAction

    data class OnSubmitCountMatch(val countMatchList: List<CountMatch>, val assessmentId: String = "", val studentId: String = ""): NumeracyAssessmentAction

    data class OnSubmitNumeracyOperations(val operationList: List<NumeracyArithmeticOperation>, val assessmentId: String = "", val studentId: String = ""): NumeracyAssessmentAction

    data class OnSubmitNumberRecognition(val numberRecognitionList: List<NumeracyNumberRecognition>, val assessmentId: String = "", val studentId: String = ""): NumeracyAssessmentAction

    data class OnSubmitWordProblem(val wordProblem: NumeracyWordProblem, val assessmentId: String = "", val studentId: String = ""): NumeracyAssessmentAction
}