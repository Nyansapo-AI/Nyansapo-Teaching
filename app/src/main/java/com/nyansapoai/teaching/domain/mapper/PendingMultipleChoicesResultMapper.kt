package com.nyansapoai.teaching.domain.mapper

import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingMultipleChoicesResult
import database.PendingMultipleChoicesResultEntity

fun PendingMultipleChoicesResultEntity.toPendingMultipleChoicesResult(): PendingMultipleChoicesResult {

    return PendingMultipleChoicesResult(
        id = id,
        assessmentId = assessmentId,
        studentId = studentId,
        multipleChoicesResult = MultipleChoicesResult(
            question = question,
            options = options.split("#"),
            student_answer = studentAnswer,
            passed = passed.toInt() == 1
        )
    )

}