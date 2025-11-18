package com.nyansapoai.teaching.presentation.assessments.numeracy

import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyAssessmentContent
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.domain.models.assessments.numeracy.numeracyAssessmentData
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel

data class NumeracyAssessmentState(

    val numeracyAssessmentContent: NumeracyAssessmentContent? = numeracyAssessmentData.numeracyAssessmentContentList[0],
    val isLoading: Boolean = false,
    val currentIndex: Int = 0,
    val numeracyArithmeticOperationRound: Int = 0,

    val countMatchAnswer: Int? = null,
    val countAndMatchResults: MutableList<CountMatch> = mutableListOf(),

    val showInstruction: Boolean = true,
    val audioFilePath: String? = null,
    val showContent: Boolean = false,

    val shouldCaptureAnswer: Boolean = false,
    val error: String? = null,
    val showResponseAlert: Boolean = false,
    val answerFilePath: String? = null,
    val workAreaFilePath: String? = null,
    val answerInt: Int? = null,

    val numberRecognitionResults: MutableList<NumeracyNumberRecognition> = mutableListOf(),
    val arithmeticOperationResults: MutableList<NumeracyArithmeticOperation> = mutableListOf(),
    val wordProblemResults: MutableList<NumeracyWordProblem> = mutableListOf(),
    val wordProblem: NumeracyWordProblem? = null,

    val currentRoundIndex: Int = 0,
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
    ),

    val showEndAssessmentDialog: Boolean = false,
    val showPreMatureAssessmentEndDialog: Boolean = false,

)