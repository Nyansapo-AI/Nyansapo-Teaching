package com.nyansapoai.teaching.data.local.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nyansapoai.teaching.Database
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.domain.mapper.toPendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.mapper.toPendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingReadingAssessmentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SQLDelightDataSourceImp(
    private val database: Database,
): LocalDataSource {

    private val assessmentQueries = database.assessmentDatabaseQueries

    override suspend fun insertPendingReadingResult(
        assessmentId: String,
        studentId: String,
        type: String,
        content: String,
        audioUrl: String,
        transcript: String,
        passed: Boolean,
        timestamp: Int,
        isPending: Boolean
    ) {
        assessmentQueries.insertPendingResult(
            assessmentId = assessmentId,
            studentId = studentId,
            type = type,
            content = content,
            audioUrl = audioUrl,
            transcript = transcript,
            passed = if (passed) 1L else 0L,
            timestamp = timestamp.toLong(),
            isPending = if (isPending) 1L else 0L
        )
    }

    override suspend fun getPendingReadingResults(assessmentId: String, studentId: String): Flow<List<PendingReadingAssessmentResult>> {
        return assessmentQueries.getPendingResultsByAssessmentandStudent(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map { it.toPendingReadingAssessmentResult() } }
            .flowOn(Dispatchers.IO)

    }

    override suspend fun markResultsAsSubmitted(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.markResultsAsSubmitted(
            assessmentId = assessmentId,
            studentId = studentId
        )
    }

    override suspend fun deleteSubmittedResults(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.deleteSubmittedResults(
            assessmentId = assessmentId,
            studentId = studentId
        )
    }

    override suspend fun insertPendingMultipleChoicesResult(
        assessmentId: String,
        studentId: String,
        question: String,
        options: List<String>,
        studentAnswer: String,
        passed: Boolean,
        timestamp: Int,
        isPending: Boolean
    ) {
        assessmentQueries.insertPendingMultipleChoicesResult(
            assessmentId = assessmentId,
            studentId = studentId,
            question = question,
            options = options.joinToString("#"),
            studentAnswer = studentAnswer,
            passed = if (passed) 1L else 0L,
            timestamp = timestamp.toLong(),
            isPending = if (isPending) 1L else 0L,
        )
    }

    override suspend fun getPendingMultipleChoicesResults(
        assessmentId: String,
        studentId: String
    ): Flow<List<PendingMultipleChoicesResult>> {
        return assessmentQueries.getPendingMultipleChoicesResultsByAssessmentandStudent(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pending -> pending.map { it.toPendingMultipleChoicesResult() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun markMultipleChoicesResultsAsSubmitted(
        studentId: String,
        assessmentId: String
    ) {
        assessmentQueries.markMultipleChoicesResultsAsSubmitted(
            assessmentId = assessmentId,
            studentId = studentId,
        )
    }

    override suspend fun clearSubmittedMultipleChoicesResults(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.deleteSubmittedResults(assessmentId = assessmentId, studentId = studentId)
    }
}