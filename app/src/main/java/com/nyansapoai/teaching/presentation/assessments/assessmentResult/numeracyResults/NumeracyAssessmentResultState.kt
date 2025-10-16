package com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults

import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumberRecognitionResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.WordProblemResult
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.ImageResult

data class NumeracyAssessmentResultState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val count_and_match: List<CountMatch> = emptyList(),
    val additions: List<NumeracyOperationResult> = emptyList(),
    val subtractions: List<NumeracyOperationResult> = emptyList(),
    val multiplications: List<NumeracyOperationResult> = emptyList(),
    val divisions: List<NumeracyOperationResult> = emptyList(),
    val word_problem: List<WordProblemResult> = emptyList(),
    val number_recognition: List<NumberRecognitionResult> = emptyList(),
    val selectedNumberRecognition: NumberRecognitionResult? = null,
    val selectedImageResult: ImageResult? = null,
)