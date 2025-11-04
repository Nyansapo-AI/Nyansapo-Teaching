package com.nyansapoai.teaching.data.local.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.nyansapoai.teaching.Database
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.mapper.assessment.toCompletedAssessment
import com.nyansapoai.teaching.domain.mapper.assessment.toNumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.mapper.assessment.toNumeracyWordProblem
import com.nyansapoai.teaching.domain.mapper.assessment.toPendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.mapper.assessment.toPendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.mapper.school.toLocalSchoolInfo
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SQLDelightDataSourceImp(
    private val database: Database,
): LocalDataSource {

    private val assessmentQueries = database.assessmentDatabaseQueries
    private val schoolDatabaseQueries = database.schoolDatabaseQueries

    override suspend fun clearAllData() {
        assessmentQueries.transaction {
            schoolDatabaseQueries.clearSchoolInfo()
        }
    }

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
            isCompleted = 1
        )
    }

    override fun completeAssessment(
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

    override suspend fun fetchCompletedAssessments(assessmentId: String): Flow<List<CompletedAssessment>> {
        return assessmentQueries.getCompletedAssessments(assessmentId = assessmentId)
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

    override suspend fun insertPendingNumeracyOperation(
        assessmentId: String,
        studentId: String,
        numeracyArithmeticOperation: NumeracyArithmeticOperation
    ) {
        assessmentQueries.insertPendingNumeracyArithmeticResult(
            assessmentId = assessmentId,
            studentId = studentId,
            operationType = numeracyArithmeticOperation.type,
            expectedAnswer = numeracyArithmeticOperation.expected_answer.toLong(),
            answer = numeracyArithmeticOperation.student_answer?.toLong(),
            operand1 = numeracyArithmeticOperation.operationNumber1.toLong(),
            operand2 = numeracyArithmeticOperation.operationNumber2.toLong(),
            workAreaImageUrl = numeracyArithmeticOperation.metadata?.workAreaMediaUrl,
            answerImageUrl = numeracyArithmeticOperation.metadata?.answerMediaUrl,
            passed = if (numeracyArithmeticOperation.metadata?.passed == true) 1L else 0L
        )
    }

    override suspend fun getPendingNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String
    ): Flow<List<NumeracyArithmeticOperation>> {
        return assessmentQueries.getPendingNumeracyArithmeticResults(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map { it.toNumeracyArithmeticOperation() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearPendingNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.clearPendingNumeracyArithmeticResults(assessmentId, studentId)
    }

    override fun insertPendingNumeracyWordProblemResult(
        assessmentId: String,
        studentId: String,
        numeracyWordProblem: NumeracyWordProblem
    ) {
        assessmentQueries.insertPendingNumeracyWordProblemResult(
            assessmentId = assessmentId,
            studentId = studentId,
            question = numeracyWordProblem.question,
            studentAnswer = numeracyWordProblem.studentAnswer?.toLong(),
            expectedAnswer = numeracyWordProblem.expectedAnswer.toLong(),
            passed = if (numeracyWordProblem.metadata?.passed == true) 1L else 0L,
            workAreaImageUrl = numeracyWordProblem.metadata?.workAreaMediaUrl,
            answerImageUrl = numeracyWordProblem.metadata?.answerMediaUrl,
        )
    }

    override fun getPendingNumeracyWordProblems(
        assessmentId: String,
        studentId: String
    ): Flow<List<NumeracyWordProblem>> {
        return assessmentQueries.getPendingNumeracyWordProblemResults(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map { it.toNumeracyWordProblem() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearPendingNumeracyWordProblems(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.clearPendingNumeracyWordProblemResults(assessmentId, studentId)
    }

    override suspend fun insertPendingCountMatch(
        assessmentId: String,
        studentId: String,
        countList: List<CountMatch>
    ) {
        assessmentQueries.transaction {
            countList.forEach { countMatch ->
                assessmentQueries.insertPendingCountAndMatchResult(
                    assessmentId = assessmentId,
                    studentId = studentId,
                    expectedNumber = countMatch.expected_number?.toLong() ?: 0,
                    studentAnswer = countMatch.student_count?.toLong() ?: 0,
                    passed = if (countMatch.passed == true) 1L else 0L,
                )
            }
        }
    }

    override suspend fun getPendingCountMatches(
        assessmentId: String,
        studentId: String
    ): Flow<List<CountMatch>> {
        return assessmentQueries.getPendingCountAndMatchResults(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map {
                CountMatch(
                    expected_number = it.expectedNumber.toInt(),
                    student_count = it.studentAnswer?.toInt(),
                    passed = it.passed == 1L,
                )
            } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearPendingCountMatches(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.clearPendingCountAndMatchResults(assessmentId, studentId)
    }
}