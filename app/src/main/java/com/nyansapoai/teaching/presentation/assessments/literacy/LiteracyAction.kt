package com.nyansapoai.teaching.presentation.assessments.literacy

sealed interface LiteracyAction {

    data class SetIds(
        val assessmentId: String,
        val studentId: String,
    ) : LiteracyAction
    data class SetCurrentIndex(val index: Int) : LiteracyAction

    data class SetShowContent(val showContent: Boolean) : LiteracyAction

    data class SetShowInstructions(val showInstructions: Boolean) : LiteracyAction

    data class SetAudioByteArray(val audioByteArray: ByteArray) : LiteracyAction

    data class SetAudioFilePath(val audioFilePath: String) : LiteracyAction

    data class SetResponse(val response: String?) : LiteracyAction

    data class OnSubmitResponse(
        val assessmentId: String,
        val studentId: String,
    ) : LiteracyAction


    data class OnSubmitLiteracyResults(
        val assessmentId: String,
        val studentId: String,
    ): LiteracyAction

    data class SetSelectedChoice(val selectedChoice: String) : LiteracyAction

    data class SetMultipleQuestionOptions(
        val options: List<String>,
    ) : LiteracyAction

    data class OnSubmitMultipleChoiceResponse(
        val assessmentId: String,
        val studentId: String,
    ) : LiteracyAction

    data class OnShowEndAssessmentDialogChange( val show: Boolean) : LiteracyAction

    data class OnShowPrematureEndAssessmentDialogChange(val show: Boolean): LiteracyAction
    data object OnEndAssessment : LiteracyAction
    data object OnCompletePreTest : LiteracyAction
}