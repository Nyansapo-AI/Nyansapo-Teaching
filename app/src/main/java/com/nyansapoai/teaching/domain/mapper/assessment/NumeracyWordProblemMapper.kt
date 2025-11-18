package com.nyansapoai.teaching.domain.mapper.assessment

import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationMetadata
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import database.PendingNumeracyWordProblemResultEntity

fun PendingNumeracyWordProblemResultEntity.toNumeracyWordProblem(): NumeracyWordProblem {
    return NumeracyWordProblem(
        question = question,
        expectedAnswer = expectedAnswer.toInt(),
        studentAnswer = studentAnswer?.toInt(),
        metadata = NumeracyOperationMetadata(
            workAreaMediaUrl = workAreaImageUrl,
            answerMediaUrl = answerImageUrl,
            passed = passed == 1L
        )
    )
}