package com.nyansapoai.teaching.data.remote.assessment

import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
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
}