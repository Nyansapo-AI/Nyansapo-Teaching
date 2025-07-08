package com.nyansapoai.teaching.presentation.assessments.literacy.use_cases

import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.utils.Results

interface LiteracyAssessmentHandler {
    suspend fun evaluateReadingAssessmentResponse(audioByteArray: ByteArray, content: String, type: String): Results<ReadingAssessmentResult>

    suspend fun addToMultipleChoiceResults(): MultipleChoicesResult?

    suspend fun submitReadingAssessment(
        assessmentId: String,
        studentID: String,
        readingAssessmentResults: List<ReadingAssessmentResult>
    ): Results<String>

}