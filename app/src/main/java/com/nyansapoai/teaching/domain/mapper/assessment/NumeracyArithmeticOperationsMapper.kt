package com.nyansapoai.teaching.domain.mapper.assessment

import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationMetadata
import database.PendingNumeracyArithmreticResultEntity

fun PendingNumeracyArithmreticResultEntity.toNumeracyArithmeticOperation(): NumeracyArithmeticOperation {
    return NumeracyArithmeticOperation(
        type = operationType,
        expected_answer = expectedAnswer.toInt(),
        student_answer = answer?.toInt(),
        operationNumber1 = operand1.toInt(),
        operationNumber2 = operand2.toInt(),
        metadata = NumeracyOperationMetadata(
            workAreaMediaUrl = workAreaImageUrl,
            answerMediaUrl = answerImageUrl,
            passed = passed == 1L
        )
    )
}