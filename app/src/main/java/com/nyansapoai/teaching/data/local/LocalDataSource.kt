package com.nyansapoai.teaching.data.local

import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
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

    suspend fun saveCurrentSchoolInfo(organizationUid: String, projectUid: String, schoolUid: String)

    suspend fun getSavedCurrentSchoolInfo(): Flow<LocalSchoolInfo>

    suspend fun insertCompletedAssessment(studentId:String, assessmentId: String,)

    suspend fun completeAssessment(studentId:String, assessmentId: String,isCompleted: Boolean )

    suspend fun getCompletedAssessments(assessmentId: String): Flow<List<CompletedAssessment>>

    suspend fun insertLiteracyAssessmentWorkerRequest(assessmentId: String, studentId: String, requestId: String, type: String)

    suspend fun getLiteracyAssessmentWorkerRequests(assessmentId: String, studentId: String, type: String): Flow<List<String>>

    suspend fun clearLiteracyAssessmentRequests(assessmentId: String, studentId: String, type: String)

}