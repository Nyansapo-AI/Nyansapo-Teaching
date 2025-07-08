package com.nyansapoai.teaching.data.local

import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingReadingAssessmentResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertPendingReadingResult(assessmentId: String,studentId: String,type: String, content: String, audioUrl: String, transcript: String, passed: Boolean, timestamp: Int, isPending: Boolean)

    suspend fun getPendingReadingResults(assessmentId: String,studentId: String): Flow<List<PendingReadingAssessmentResult>>

    suspend fun markResultsAsSubmitted(assessmentId: String, studentId: String)

    suspend fun deleteSubmittedResults(assessmentId: String, studentId: String)

    suspend fun insertPendingMultipleChoicesResult(assessmentId: String, studentId: String, question: String, options: List<String>, studentAnswer: String, passed: Boolean, timestamp: Int, isPending: Boolean)

    suspend fun getPendingMultipleChoicesResults(assessmentId: String, studentId: String): Flow<List<PendingMultipleChoicesResult>>

    suspend fun markMultipleChoicesResultsAsSubmitted(studentId: String, assessmentId: String)

    suspend fun clearSubmittedMultipleChoicesResults(assessmentId: String, studentId: String)

}