package com.nyansapoai.teaching.presentation.assessments.literacy.result

sealed interface LiteracyResultAction {
    data class OnSelectAudioUrl(val audioUrl: String?) : LiteracyResultAction
}