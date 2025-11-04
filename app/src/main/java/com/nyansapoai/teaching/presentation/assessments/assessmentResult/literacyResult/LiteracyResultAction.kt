package com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult

import com.nyansapoai.teaching.presentation.assessments.assessmentResult.AudioResult
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.ImageResult

sealed interface LiteracyResultAction {
    data class OnSelectImageResult(val imageResult: ImageResult?) : LiteracyResultAction
    data class OnSelectAudioResult(val audioResult: AudioResult?) : LiteracyResultAction
}