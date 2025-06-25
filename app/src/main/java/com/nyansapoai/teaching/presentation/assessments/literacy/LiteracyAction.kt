package com.nyansapoai.teaching.presentation.assessments.literacy

sealed interface LiteracyAction {
    data class SetCurrentIndex(val index: Int) : LiteracyAction

    data class SetShowContent(val showContent: Boolean) : LiteracyAction

    data class SetShowInstructions(val showInstructions: Boolean) : LiteracyAction

    data class SetAudioByteArray(val audioByteArray: ByteArray) : LiteracyAction

    data class SetResponse(val response: String?) : LiteracyAction

    data class OnSubmitResponse(
        val assessmentId: String,
        val studentId: String,
        val response: String,
    ) : LiteracyAction

}