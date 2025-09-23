package com.nyansapoai.teaching.data.remote.assessment

import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface  AssessmentRepository {
    suspend fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<NyansapoStudent>,
        schoolId: String = "",
        organizationId: String = "",
        projectId: String = ""
    ): Results<Unit>

    suspend fun getAssessments(schoolId: String = ""): Flow<List<Assessment>>

    suspend fun getAssessmentById(assessmentId: String): Flow<Results<Assessment>>

    suspend fun assessNumeracyCountAndMatch(
        assessmentId: String,
        studentID: String,
        countAndMatchList: List<CountMatch>
    ): Results<String>


    suspend fun assessNumeracyNumberRecognition(
        assessmentId: String,
        studentID: String,
        numberRecognitionList: List<String>
    ): Results<String>


    suspend fun assessNumeracyArithmeticOperations(
        assessmentId: String,
        studentID: String,
        arithmeticOperations: List<NumeracyArithmeticOperation>
    ): Results<String>

    suspend fun assessNumeracyWordProblem(
        assessmentId: String,
        studentID: String,
        wordProblemList: List<NumeracyWordProblem>
    ): Results<String>


    suspend fun assessReadingAssessment(
        assessmentId: String,
        studentID: String,
        readingAssessmentResults: List<ReadingAssessmentResult>
    ): Results<String>

    suspend fun addReadingAssessmentResult(
        assessmentId: String,
        studentID: String,
        readingAssessment: ReadingAssessmentResult
    ): Results<String>

    suspend fun assessMultipleChoiceQuestions(
        assessmentId: String,
        studentID: String,
        multipleChoiceQuestions: List<MultipleChoicesResult>
    ): Results<String>

    suspend fun markLiteracyAssessmentAsComplete(assessmentId: String, studentId: String): Results<String>

    fun getCompletedAssessments(assessmentId: String): Flow<Results<List<CompletedAssessment>>>
}