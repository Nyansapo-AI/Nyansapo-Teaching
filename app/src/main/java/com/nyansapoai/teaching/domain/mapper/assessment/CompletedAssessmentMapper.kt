package com.nyansapoai.teaching.domain.mapper.assessment

import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import database.CompleteAssessmentEntity

fun CompleteAssessmentEntity.toCompletedAssessment(): CompletedAssessment {
    return CompletedAssessment(
        assessmentId = assessmentId,
        student_id = studentId,
        completed_assessment = isCompleted.toInt() == 1
    )
}