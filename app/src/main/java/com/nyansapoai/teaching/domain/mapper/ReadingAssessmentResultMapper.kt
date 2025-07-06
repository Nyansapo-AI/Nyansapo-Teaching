package com.nyansapoai.teaching.domain.mapper

import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentMetadata
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import database.PendingReadingResult

fun PendingReadingResult.toReadingAssessmentResult() : ReadingAssessmentResult {
    return ReadingAssessmentResult(
        type = type,
        content = content,
        metadata = ReadingAssessmentMetadata(
            audio_url = audioUrl,
            passed = passed.toInt() == 1,
            transcript = transcript
        )
    )
}