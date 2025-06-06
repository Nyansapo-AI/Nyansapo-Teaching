package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyAssessmentContent
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation

@Composable
fun NumeracyContent(
    modifier: Modifier = Modifier,
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
    shouldCaptureAnswer: Boolean = false,
    onCaptureWorkAreaContent: (ByteArray) -> Unit = {},
    assessmentLevel: NumeracyAssessmentLevel = NumeracyAssessmentLevel.ADDITION
) {
    AnimatedContent(
        targetState = assessmentLevel
    ) { level ->

//        var additionIndex by remember { mutableStateOf(0) }

        when (level) {
            NumeracyAssessmentLevel.COUNT_MATCH -> {

                NumeracyCountAndMatch(
                    count = assessmentContent.countAndMatchNumbersList[countMatchIndex],
                    onSubmit = onSubmitCountMatch
                )
            }
            NumeracyAssessmentLevel.ADDITION -> {
                AppSimulateNavigation(
                    targetState = additionIndex
                ){
                    NumeracyOperation(
                        modifier = modifier,
                        firstNumber = assessmentContent.additions[additionIndex].firstNumber,
                        secondNumber = assessmentContent.additions[additionIndex].secondNumber,
                        operationType = assessmentContent.additions[additionIndex].operationType,
                        operationOrientation = Orientation.Vertical,
                        onCaptureAnswerContent = onCaptureAnswerContent,
                        shouldCaptureAnswer = shouldCaptureAnswer,
                        onCaptureWorkAreaContent = onCaptureWorkAreaContent,
                        shouldCaptureWorkArea = shouldCaptureAnswer,
                        onSubmit = onSubmitAddition
//                        onSubmit = {
//                            onSubmitAddition()
//                            additionIndex = (additionIndex + 1) % assessmentContent.additions.size
//                        }
                    )
                }
            }
            NumeracyAssessmentLevel.SUBTRACTION -> {
                AnimatedContent(
                    targetState = subtractionIndex
                ) { index ->
                    when(index){
                        else -> {
                            NumeracyOperation(
                                modifier = modifier,
                                firstNumber = assessmentContent.subtractions[subtractionIndex].firstNumber,
                                secondNumber = assessmentContent.subtractions[subtractionIndex].secondNumber,
                                operationType = assessmentContent.subtractions[subtractionIndex].operationType,
                                operationOrientation = Orientation.Vertical,
                                onCaptureAnswerContent = {},
                                shouldCaptureAnswer = false,
                                onCaptureWorkAreaContent = {},
                                shouldCaptureWorkArea = false,
                                onSubmit = onSubmitAddition
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
                            NumeracyOperation(
                                modifier = modifier,
                                firstNumber = assessmentContent.multiplications[multiplicationIndex].firstNumber,
                                secondNumber = assessmentContent.multiplications[multiplicationIndex].secondNumber,
                                operationType = assessmentContent.multiplications[multiplicationIndex].operationType,
                                operationOrientation = Orientation.Vertical,
                                onCaptureAnswerContent = {},
                                shouldCaptureAnswer = false,
                                onCaptureWorkAreaContent = {},
                                shouldCaptureWorkArea = false,
                                onSubmit = onSubmitMultiplication
                            )
                        }
                    }
                }
            }
            NumeracyAssessmentLevel.DIVISION -> {
                AppSimulateNavigation(
                    targetState = divisionIndex
                ){
                    NumeracyOperation(
                        modifier = modifier,
                        firstNumber = assessmentContent.divisions[divisionIndex].firstNumber,
                        secondNumber = assessmentContent.divisions[divisionIndex].secondNumber,
                        operationType = assessmentContent.divisions[divisionIndex].operationType,
                        operationOrientation = Orientation.Horizontal,
                        onCaptureAnswerContent = {},
                        shouldCaptureAnswer = false,
                        onCaptureWorkAreaContent = {},
                        shouldCaptureWorkArea = false,
                        onSubmit = onSubmitDivision
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
                )
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
