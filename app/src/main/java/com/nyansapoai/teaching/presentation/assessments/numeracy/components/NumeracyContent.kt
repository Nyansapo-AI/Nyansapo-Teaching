package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyAssessmentContent
import com.nyansapoai.teaching.presentation.assessments.components.InputResponseAlert
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumeracyContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    assessmentContent: NumeracyAssessmentContent,
    countMatchIndex: Int,
    additionIndex: Int,
    subtractionIndex: Int,
    multiplicationIndex: Int,
    divisionIndex: Int,
    numberRecognitionIndex: Int,
    onSubmitCountMatch: () -> Unit = {},
    onSubmitAddition: () -> Unit = {},
    onSubmitSubtraction: () -> Unit = {},
    onSubmitMultiplication: () -> Unit = {},
    onSubmitWordProblem: () -> Unit = {},
    onSubmitDivision: () -> Unit = {},
    onSubmitNumberRecognition: () -> Unit = {},
    onCaptureAnswerContent: (ByteArray) -> Unit = {},
    onSelectCountMatch: (Int) -> Unit = {},
    selectedCount: Int? = null,
    shouldCaptureAnswer: Boolean = false,
    onCaptureWorkAreaContent: (ByteArray) -> Unit = {},
    assessmentLevel: NumeracyAssessmentLevel = NumeracyAssessmentLevel.COUNT_MATCH,
    showResponseAlert: Boolean = false,
    answerResponse: Int? = null,
    responseError: String? = null,
    onDismissResponseAlert: () -> Unit = {},
    onReadAnswerImage: () -> Unit = {},
) {

    AnimatedContent(
        targetState = isLoading
    ) { loading ->
        when(loading){
            true -> {
                AppCircularLoading()
            }
            false -> {
                AnimatedContent(
                    targetState = assessmentLevel
                ) { level ->

                    InputResponseAlert(
                        showAlert = showResponseAlert,
                        responseError = responseError,
                        response = answerResponse,
                        onDismiss = onDismissResponseAlert,
                        onConfirm = {
                            when(level){
                                NumeracyAssessmentLevel.COUNT_MATCH -> onSubmitCountMatch()
                                NumeracyAssessmentLevel.ADDITION -> onSubmitAddition()
                                NumeracyAssessmentLevel.SUBTRACTION -> onSubmitSubtraction()
                                NumeracyAssessmentLevel.MULTIPLICATION -> onSubmitMultiplication()
                                NumeracyAssessmentLevel.DIVISION -> onSubmitDivision()
                                NumeracyAssessmentLevel.NUMBER_RECOGNITION -> onSubmitNumberRecognition()
                                NumeracyAssessmentLevel.WORD_PROBLEM -> onSubmitWordProblem()
                            }
                        }
                    )


                    when (level) {
                        NumeracyAssessmentLevel.COUNT_MATCH -> {

                            AppSimulateNavigation(
                                targetState = countMatchIndex
                            ){
                                NumeracyCountAndMatch(
                                    count = assessmentContent.countAndMatchNumbersList[countMatchIndex],
                                    onSelectCount = onSelectCountMatch,
                                    selectedCount = selectedCount ,
                                    onSubmit = onSubmitCountMatch
                                )
                            }

                        }
                        NumeracyAssessmentLevel.ADDITION -> {
                            AppSimulateNavigation(
                                targetState = additionIndex
                            ){
                                NumeracyOperationUI(
                                    modifier = modifier,
                                    firstNumber = assessmentContent.additions[additionIndex].firstNumber,
                                    secondNumber = assessmentContent.additions[additionIndex].secondNumber,
                                    operationType = assessmentContent.additions[additionIndex].operationType,
                                    operationOrientation = Orientation.Vertical,
                                    onCaptureAnswerContent = onCaptureAnswerContent,
                                    shouldCaptureAnswer = shouldCaptureAnswer,
                                    onCaptureWorkAreaContent = onCaptureWorkAreaContent,
                                    shouldCaptureWorkArea = shouldCaptureAnswer,
                                    onSubmit = onReadAnswerImage
                                )
                            }
                        }
                        NumeracyAssessmentLevel.SUBTRACTION -> {
                            AnimatedContent(
                                targetState = subtractionIndex
                            ) { index ->
                                when(index){
                                    else -> {
                                        NumeracyOperationUI(
                                            modifier = modifier,
                                            firstNumber = assessmentContent.subtractions[subtractionIndex].firstNumber,
                                            secondNumber = assessmentContent.subtractions[subtractionIndex].secondNumber,
                                            operationType = assessmentContent.subtractions[subtractionIndex].operationType,
                                            operationOrientation = Orientation.Vertical,
                                            onCaptureAnswerContent = onCaptureAnswerContent,
                                            shouldCaptureAnswer = shouldCaptureAnswer,
                                            onCaptureWorkAreaContent = onCaptureWorkAreaContent,
                                            shouldCaptureWorkArea = shouldCaptureAnswer,
                                            onSubmit = onReadAnswerImage
                                        )
                                    }
                                }

                            }
                        }
                        NumeracyAssessmentLevel.MULTIPLICATION -> {
                            AnimatedContent(
                                targetState = multiplicationIndex
                            ) { index ->
                                when(index){
                                    else -> {
                                        NumeracyOperationUI(
                                            modifier = modifier,
                                            firstNumber = assessmentContent.multiplications[multiplicationIndex].firstNumber,
                                            secondNumber = assessmentContent.multiplications[multiplicationIndex].secondNumber,
                                            operationType = assessmentContent.multiplications[multiplicationIndex].operationType,
                                            operationOrientation = Orientation.Horizontal,
                                            onCaptureAnswerContent = onCaptureAnswerContent,
                                            shouldCaptureAnswer = shouldCaptureAnswer,
                                            onCaptureWorkAreaContent = onCaptureWorkAreaContent,
                                            shouldCaptureWorkArea = shouldCaptureAnswer,
                                            onSubmit = onReadAnswerImage
                                        )
                                    }
                                }
                            }
                        }
                        NumeracyAssessmentLevel.DIVISION -> {
                            AppSimulateNavigation(
                                targetState = divisionIndex
                            ){
                                NumeracyOperationUI(
                                    modifier = modifier,
                                    firstNumber = assessmentContent.divisions[divisionIndex].firstNumber,
                                    secondNumber = assessmentContent.divisions[divisionIndex].secondNumber,
                                    operationType = assessmentContent.divisions[divisionIndex].operationType,
                                    operationOrientation = Orientation.Horizontal,
                                    onCaptureAnswerContent = onCaptureAnswerContent,
                                    shouldCaptureAnswer = shouldCaptureAnswer,
                                    onCaptureWorkAreaContent = onCaptureWorkAreaContent,
                                    shouldCaptureWorkArea = shouldCaptureAnswer,
                                    onSubmit = onReadAnswerImage
                                )
                            }
                        }
                        NumeracyAssessmentLevel.NUMBER_RECOGNITION -> {
                            AppSimulateNavigation(
                                targetState = numberRecognitionIndex
                            ){


                            }
                        }
                        NumeracyAssessmentLevel.WORD_PROBLEM -> {
                            NumeracyWordProblem(
                                wordProblem = assessmentContent.wordProblems[0].problem,
                                onCaptureAnswerContent = {},
                                shouldCaptureAnswer = false,
                                onCaptureWorkAreaContent = {},
                                shouldCaptureWorkArea = false,
                                onSubmit = onSubmitCountMatch
                            )
                        }
                    }
                }
            }
        }
    }



}



enum class NumeracyAssessmentLevel(
    val label: String
) {
    COUNT_MATCH("Count and Match"),
    ADDITION("Addition"),
    SUBTRACTION("Subtraction"),
    MULTIPLICATION("Multiplication"),
    DIVISION("Division"),
    NUMBER_RECOGNITION("Number Recognition"),
    WORD_PROBLEM("Word Problem"),
}
