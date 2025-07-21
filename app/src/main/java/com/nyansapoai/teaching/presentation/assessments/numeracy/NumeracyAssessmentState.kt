package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.ui.graphics.ImageBitmap
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyAssessmentContent
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.domain.models.assessments.numeracy.numeracyAssessmentData
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel

data class NumeracyAssessmentState(
    val shouldCaptureAnswer: Boolean = false,
    val shouldCaptureWorkArea: Boolean = false,
    val answerImageByteArray: ByteArray? = null,
    val workAreaImageByteArray: ByteArray? = null,

    val answerImageBitmap: ImageBitmap? = null,
    val workAreaImageBitmap: ImageBitmap? = null,
    val isSubmitting: Boolean = false,

    val countMatchAnswer: Int? = null,
    val isLoading: Boolean = false,
    val answerUri: String? = null,
    val response: Int? = null,
    val error: String? = null,
    val answerString: String? = null,
    val answerFilePath: String? = null,
    val workAreaFilePath: String? = null,
    val answerInt: Int? = null,
    val showResponseAlert: Boolean = false,

//    val numeracyAssessmentContent: Results<NumeracyAssessmentContent> = Results.success(data =numeracyAssessmentData.numeracyAssessmentContentList[0] ),
    val numeracyAssessmentContent: NumeracyAssessmentContent? = numeracyAssessmentData.numeracyAssessmentContentList[0],

    val countAndMatchResults: MutableList<CountMatch> = mutableListOf(),
    val numberRecognitionResults: MutableList<NumeracyNumberRecognition> = mutableListOf(),
    val arithmeticOperationResults: MutableList<NumeracyArithmeticOperation> = mutableListOf(),
    val wordProblem: NumeracyWordProblem? = null,

    val currentIndex: Int = 0,
    val countMatchIndex: Int = 0,
    val additionIndex: Int = 0,
    val subtractionIndex: Int = 0,
    val multiplicationIndex: Int = 0,
    val divisionIndex: Int = 0,
    val numberRecognitionIndex: Int = 0,
    val hasCompletedAssessment: Boolean = false,

    val numeracyLevel: NumeracyAssessmentLevel = NumeracyAssessmentLevel.COUNT_MATCH,
    val numeracyAssessmentFlow: List<NumeracyAssessmentLevel> = listOf(
        NumeracyAssessmentLevel.COUNT_MATCH,
        NumeracyAssessmentLevel.NUMBER_RECOGNITION,
        NumeracyAssessmentLevel.ADDITION,
        NumeracyAssessmentLevel.SUBTRACTION,
        NumeracyAssessmentLevel.MULTIPLICATION,
        NumeracyAssessmentLevel.DIVISION,
        NumeracyAssessmentLevel.WORD_PROBLEM,
    )
)