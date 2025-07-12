package com.nyansapoai.teaching.domain.mapper.assessment

import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingReadingAssessmentResult
import database.PendingReadingResult

fun PendingReadingResult.toPendingReadingAssessmentResult(): PendingReadingAssessmentResult {
    return PendingReadingAssessmentResult(
        assessmentId = assessmentId,
        studentId = studentId,
        readingAssessmentResult = this.toReadingAssessmentResult()
    )
}