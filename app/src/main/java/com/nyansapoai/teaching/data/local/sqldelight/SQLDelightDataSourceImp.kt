package com.nyansapoai.teaching.data.local.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.nyansapoai.teaching.Database
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.mapper.assessment.toCompletedAssessment
import com.nyansapoai.teaching.domain.mapper.assessment.toPendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.mapper.assessment.toPendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.mapper.school.toLocalSchoolInfo
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
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
    private val schoolDatabaseQueries = database.schoolDatabaseQueries

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

    override suspend fun saveCurrentSchoolInfo(
        organizationUid: String,
        projectUid: String,
        schoolUid: String
    ) {
        schoolDatabaseQueries.transaction{

            schoolDatabaseQueries.clearSchoolInfo()

            schoolDatabaseQueries.insertSchoolInfo(
                organizationUID = organizationUid,
                projectUID = projectUid,
                schoolUID = schoolUid
            )
        }
    }

    override fun getSavedCurrentSchoolInfo(): Flow<LocalSchoolInfo> {
        return schoolDatabaseQueries.getSchoolInfo()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map {  it -> it.toLocalSchoolInfo() }
            .flowOn(Dispatchers.Main)
    }

    override suspend fun insertCompletedAssessment(
        studentId: String,
        assessmentId: String
    ) {
        assessmentQueries.insertCompleteAssessment(
            assessmentId = assessmentId,
            studentId = studentId,
            isCompleted = 0
        )
    }

    override suspend fun completeAssessment(
        studentId: String,
        assessmentId: String,
        isCompleted: Boolean
    ) {
        assessmentQueries.updateAssessmentCompletion(
            studentId = studentId,
            assessmentId = assessmentId,
            isCompleted = if (isCompleted) 1 else 0
        )
    }

    override suspend fun getCompletedAssessments(assessmentId: String): Flow<List<CompletedAssessment>> {
        return assessmentQueries.getCompletedAssessments()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {  it.map { assessment -> assessment.toCompletedAssessment() } }
            .flowOn(Dispatchers.IO )
    }

    override suspend fun insertLiteracyAssessmentWorkerRequest(
        assessmentId: String,
        studentId: String,
        requestId: String,
        type: String
    ) {
        assessmentQueries.insertLIteracyAssessmentWorkerRequest(
            requestId = requestId,
            assessmentId = assessmentId,
            studentId = studentId,
            assessmentType = type
        )
    }

    override suspend fun getLiteracyAssessmentWorkerRequests(
        assessmentId: String,
        studentId: String,
        type: String
    ): Flow<List<String>> {
        return assessmentQueries.getLIteracyAssessmentWorkerRequestByAssessmentIDandStudentIDandAssessmentType(
            assessmentId = assessmentId,
            studentId = studentId,
            assessmentType = type
        )
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { request -> request.requestId } }
            .flowOn(Dispatchers.Main)
    }

    override suspend fun clearLiteracyAssessmentRequests(
        assessmentId: String,
        studentId: String,
        type: String
    ) {
        assessmentQueries.clearLIteracyAssessmentWorkerRequestByAssessmentIDandStudentIDandAssessmentType(
            assessmentId = assessmentId,
            studentId = studentId,
            assessmentType = type
        )
    }
}