package com.nyansapoai.teaching.domain.models.assessments.numeracy

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class NumeracyAssessmentResults(
    val assessmentId: String = "",
    val student_id: String = "",
    val student_grade: Int? =  null,
    val numeracy_results : NumeracyResultsData? = null,
)
@IgnoreExtraProperties
data class NumeracyResultsData(
    val count_and_match: List<CountMatch> = emptyList(),
    val number_operations: List<NumeracyOperationResult> = emptyList(),
    val word_problem: List<WordProblemResult> = emptyList(),
    val number_recognition: List<NumberRecognitionResult> = emptyList()

    )
@IgnoreExtraProperties
data class NumeracyOperationResult(
    val expected_answer: String? = null,
    val metadata: NumeracyOperationResultMetadata  = NumeracyOperationResultMetadata(),
    val operations_number1: String? = null,
    val operations_number2: String? = null,
    val student_answer: String? = null,
    val type: String = "",
)

@IgnoreExtraProperties
data class NumeracyOperationResultMetadata(
    val transcript: String? = null,
    val screenshot_url: String? = null,
    val passed: Boolean? = null
)

@IgnoreExtraProperties
data class WordProblemResult(
    val expected_number: String = "",
    val metadata: WordProblemResultMetadata= WordProblemResultMetadata(),
    val question: String? = null,
    val type: String = "",
)

@IgnoreExtraProperties
data class WordProblemResultMetadata(
    val student_answer: String? = null,
    val transcript: String? = null,
    val screenshot_url: String? = null,
    val passed: Boolean? = null,
)

@IgnoreExtraProperties
data class NumberRecognitionResult(
    val content: String = "",
    val metadata: NumberRecognitionResultMetadata = NumberRecognitionResultMetadata()
)

@IgnoreExtraProperties
data class NumberRecognitionResultMetadata(
    val type: String = "",
    val transcript: String? = null,
    val audio_url: String? = null,
    val passed: Boolean? = null
)