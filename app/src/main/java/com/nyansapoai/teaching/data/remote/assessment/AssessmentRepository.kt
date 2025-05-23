package com.nyansapoai.teaching.data.remote.assessment

interface AssessmentRepository {
    suspend fun createAssessment(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        totalMarks: Int,
        passingMarks: Int,
        duration: Int,
        subjectId: String
    ): Boolean
}