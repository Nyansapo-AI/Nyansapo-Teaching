package com.nyansapoai.teaching.presentation.assessments.numeracy

import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyAssessmentData
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyNumberRecognition
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.domain.models.assessments.numeracy.WordProblem
import com.nyansapoai.teaching.utils.Results

data class NumeracyAssessmentState(
    val shouldCaptureAnswer: Boolean = false,
    val shouldCaptureWorkArea: Boolean = false,
    val answerImageByteArray: ByteArray? = null,
    val workAreaImageByteArray: ByteArray? = null,
    val answerString: String? = null,
    val answerInt: Int? = null,
    val numeracyAssessmentContent: Results<NumeracyAssessmentData> = Results.initial(),

    val countAndMatchResults: MutableList<CountMatch> = mutableListOf(),
    val numberRecognitionResults: MutableList<NumeracyNumberRecognition> = mutableListOf(),
    val additionOperationResults: MutableList<NumeracyArithmeticOperation> = mutableListOf(),
    val subtractionOperationResults: MutableList<NumeracyArithmeticOperation> = mutableListOf(),
    val multiplicationOperationResults: MutableList<NumeracyArithmeticOperation> = mutableListOf(),
    val divisionOperationResults: MutableList<NumeracyArithmeticOperation> = mutableListOf(),
    val wordProblem: NumeracyWordProblem? = null

)