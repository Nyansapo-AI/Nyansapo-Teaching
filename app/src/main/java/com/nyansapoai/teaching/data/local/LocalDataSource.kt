package com.nyansapoai.teaching.data.local

import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun clearAllData()

    suspend fun insertPendingReadingResult(assessmentId: String,studentId: String,type: String, content: String, audioUrl: String, transcript: String, passed: Boolean, timestamp: Int, isPending: Boolean)

    suspend fun getPendingReadingResults(assessmentId: String,studentId: String): Flow<List<PendingReadingAssessmentResult>>

    suspend fun markResultsAsSubmitted(assessmentId: String, studentId: String)

    suspend fun deleteSubmittedResults(assessmentId: String, studentId: String)

    suspend fun insertPendingMultipleChoicesResult(assessmentId: String, studentId: String, question: String, options: List<String>, studentAnswer: String, passed: Boolean, timestamp: Int, isPending: Boolean)

    suspend fun getPendingMultipleChoicesResults(assessmentId: String, studentId: String): Flow<List<PendingMultipleChoicesResult>>

    suspend fun markMultipleChoicesResultsAsSubmitted(studentId: String, assessmentId: String)

    suspend fun clearSubmittedMultipleChoicesResults(assessmentId: String, studentId: String)

    suspend fun saveCurrentSchoolInfo(organizationUid: String, projectUid: String, schoolUid: String)

    fun getSavedCurrentSchoolInfo(): Flow<LocalSchoolInfo>

    suspend fun insertCompletedAssessment(studentId:String, assessmentId: String,)

    fun completeAssessment(studentId:String, assessmentId: String,isCompleted: Boolean )

    suspend fun fetchCompletedAssessments(assessmentId: String): Flow<List<CompletedAssessment>>

    suspend fun insertLiteracyAssessmentWorkerRequest(assessmentId: String, studentId: String, requestId: String, type: String)

    suspend fun getLiteracyAssessmentWorkerRequests(assessmentId: String, studentId: String, type: String): Flow<List<String>>

    suspend fun clearLiteracyAssessmentRequests(assessmentId: String, studentId: String, type: String)

    suspend fun insertPendingNumeracyOperation(assessmentId: String, studentId: String,numeracyArithmeticOperation: NumeracyArithmeticOperation)

    suspend fun getPendingNumeracyArithmeticOperations(assessmentId: String, studentId: String): Flow<List<NumeracyArithmeticOperation>>

    suspend fun clearPendingNumeracyArithmeticOperations(assessmentId: String, studentId: String)

    fun insertPendingNumeracyWordProblemResult(
        assessmentId: String,
        studentId: String,
        numeracyWordProblem: NumeracyWordProblem
    )

    fun getPendingNumeracyWordProblems(
        assessmentId: String,
        studentId: String
    ): Flow<List<NumeracyWordProblem>>

    suspend fun clearPendingNumeracyWordProblems(assessmentId: String, studentId: String)

    suspend fun insertPendingCountMatch(assessmentId: String, studentId: String, countList: List<CountMatch>)

    suspend fun getPendingCountMatches(assessmentId: String, studentId: String): Flow<List<CountMatch>>

    suspend fun clearPendingCountMatches(assessmentId: String, studentId: String)

}