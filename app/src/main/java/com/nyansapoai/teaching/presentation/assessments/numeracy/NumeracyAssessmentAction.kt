package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.ui.graphics.ImageBitmap
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel

sealed interface NumeracyAssessmentAction {
//    data class OnCaptureAnswer(val imageByteArray: ByteArray) : NumeracyAssessmentAction
//    data class OnCaptureWorkArea(val imageByteArray: ByteArray) : NumeracyAssessmentAction
//    data class OnAnswerImageBitmapChange(val imageBitmap: ImageBitmap?) : NumeracyAssessmentAction
//    data class OnWorkAreaImageBitmapChange(val imageBitmap: ImageBitmap?) : NumeracyAssessmentAction
    data class OnAnswerImageFilePathChange(val path: String) : NumeracyAssessmentAction
    data class OnWorkAreaImageFilePathChange(val path: String) : NumeracyAssessmentAction
    data class OnIsSubmittingChange(val isSubmitting: Boolean) : NumeracyAssessmentAction
    /*
    data class OnShouldCaptureWorkAreaChange(val shouldCapture: Boolean) : NumeracyAssessmentAction
    */
    data class OnShowResponseAlertChange(val showResponseAlert: Boolean) : NumeracyAssessmentAction
    data class OnCountMatchAnswerChange(val countMatchAnswer: Int) : NumeracyAssessmentAction
//    data class OnReadAnswerImage(val imageByteArray: ByteArray?) : NumeracyAssessmentAction
//    data class OnAddCountMatch(val countMatch: Int, val onSuccess: () -> Unit) : NumeracyAssessmentAction
    data class OnAddArithmeticOperation(val numeracyOperations: NumeracyOperations, val onSuccess: () -> Unit) : NumeracyAssessmentAction
    data class OnAddNumberRecognition(val numberRecognition: NumeracyNumberRecognition, val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitCountMatch(val countMatch: List<CountMatch>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitNumeracyOperations(val operationList: List<NumeracyArithmeticOperation>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitNumberRecognition(val numberRecognitionList: List<NumeracyNumberRecognition>, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnSubmitWordProblem(val wordProblem: NumeracyWordProblem, val assessmentId: String = "", val studentId: String = "", val onSuccess: () -> Unit): NumeracyAssessmentAction
    data class OnNumeracyLevelChange(val numeracyLevel: NumeracyAssessmentLevel): NumeracyAssessmentAction
    data object OnSubmitAnswer : NumeracyAssessmentAction
    data object OnClearAnswer : NumeracyAssessmentAction
    data object OnClearWorkArea : NumeracyAssessmentAction
}