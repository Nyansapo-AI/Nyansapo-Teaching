package com.nyansapoai.teaching.data.remote.assessment

import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    suspend fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<AssignedStudent>
    ): Results<Unit>

    suspend fun getAssessments(): Flow<List<Assessment>>

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
        wordProblem: NumeracyWordProblem
    ): Results<String>


    suspend fun assessReadingAssessment(
        assessmentId: String,
        studentID: String,
        readingAssessmentResults: List<ReadingAssessmentResult>
    ): Results<String>

    suspend fun assessMultipleChoiceQuestions(
        assessmentId: String,
        studentID: String,
        multipleChoiceQuestions: List<MultipleChoicesResult>
    ): Results<String>
}