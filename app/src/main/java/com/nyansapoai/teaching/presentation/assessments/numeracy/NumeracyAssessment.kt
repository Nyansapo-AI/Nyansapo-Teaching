package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyCountAndMatch
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyOperationContainerUI
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
    assessmentNo: Int
) {

    val viewModel = koinViewModel<NumeracyAssessmentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumeracyAssessmentScreen(
        state = state,
        onAction = viewModel::onAction,
        studentId = studentId,
        assessmentId = assessmentId,
        modifier = modifier
    )
}

@Composable
fun NumeracyAssessmentScreen(
    modifier: Modifier = Modifier,
    state: NumeracyAssessmentState,
    onAction: (NumeracyAssessmentAction) -> Unit,
    studentId: String,
    assessmentId: String
) {

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding  ->

        if (state.numeracyAssessmentContent == null) {
            Text(
                text = "No Questions available",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            return@Scaffold
        }


        if (state.isLoading){
            AnimatedVisibility(
                visible = state.isLoading,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                ) {
                    LinearProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(8.dp)
                            .width(200.dp),
                    )

                }
            }
            return@Scaffold
        }

        AppSimulateNavigation(
            targetState = state.numeracyLevel,
            modifier = Modifier
                .padding(innerPadding)
        ){
            when(state.numeracyLevel) {
                NumeracyAssessmentLevel.COUNT_MATCH -> {
                    NumeracyCountAndMatch(
                        countList = state.numeracyAssessmentContent.countAndMatchNumbersList,
                        currentIndex = state.currentIndex,
                        onSelectCount = { count ->
                            onAction(
                                NumeracyAssessmentAction.OnCountMatchAnswerChange(countMatchAnswer = count)
                            )
                        },
                        selectedCount = state.countMatchAnswer,
                        onSubmit = {
                            onAction.invoke(
                                NumeracyAssessmentAction.OnSubmitCountMatch(
                                    countMatch = emptyList(),
                                    assessmentId = assessmentId,
                                    studentId = studentId,
                                    onSuccess = {}
                                )
                            )
                        }
                    )
                }
                NumeracyAssessmentLevel.ADDITION ,
                NumeracyAssessmentLevel.SUBTRACTION ,
                NumeracyAssessmentLevel.MULTIPLICATION,
                NumeracyAssessmentLevel.DIVISION -> {

                    val operationList = when(state.numeracyLevel) {
                        NumeracyAssessmentLevel.ADDITION -> state.numeracyAssessmentContent.additions
                        NumeracyAssessmentLevel.SUBTRACTION -> state.numeracyAssessmentContent.subtractions
                        NumeracyAssessmentLevel.MULTIPLICATION -> state.numeracyAssessmentContent.multiplications
                        NumeracyAssessmentLevel.DIVISION -> state.numeracyAssessmentContent.divisions
                        else -> emptyList()
                    }


                    NumeracyOperationContainerUI(
                        numeracyOperationList = operationList,
                        currentIndex = state.currentIndex,
                        onAnswerFilePathChange = {path ->
                            onAction.invoke(
                                NumeracyAssessmentAction.OnAnswerImageFilePathChange(path = path)
                            )
                        },
                        onWorkOutFilePathChange = { path ->
                            onAction.invoke(
                                NumeracyAssessmentAction.OnWorkAreaImageFilePathChange(path = path)
                            )
                        },
                        isLoading = state.isLoading,
                        shouldCapture = state.shouldCaptureAnswer,
                        onSubmit = {
                            onAction.invoke(
                                NumeracyAssessmentAction.OnSubmitNumeracyOperations(
                                    operationList = emptyList(),
                                    assessmentId = assessmentId,
                                    studentId = studentId,
                                    onSuccess = {}
                                )
                            )
                        }
                    )
                }
                NumeracyAssessmentLevel.NUMBER_RECOGNITION -> {
                    /*
                    LiteracyReadingAssessmentUI(

                    )

                     */
                }
                NumeracyAssessmentLevel.WORD_PROBLEM -> {}
            }

        }

        /*
        AnimatedContent(
            targetState = isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        )
        { loading ->
            when(loading) {
                true -> {
                    AppLottieAnimations(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                false -> {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .fillMaxSize()
                    ) {

                        state.numeracyAssessmentContent.data?.let {
                            NumeracyContent(
                                modifier = Modifier
                                    .fillMaxSize(),
                                isLoading = state.isLoading,
                                assessmentContent = numeracyAssessmentData.numeracyAssessmentContentList[0],
                                countMatchIndex = state.countMatchIndex,
                                subtractionIndex = state.subtractionIndex,
                                multiplicationIndex = state.multiplicationIndex,
                                divisionIndex = state.divisionIndex,
                                numberRecognitionIndex = state.numberRecognitionIndex,
                                assessmentLevel = state.numeracyLevel,
                                onReadAnswerImage = {
                                    onAction(
                                        NumeracyAssessmentAction.OnReadAnswerImage(imageByteArray = state.answerImageByteArray)
                                    )
                                },
                                onSelectCountMatch = { answer ->
                                    onAction(
                                        NumeracyAssessmentAction.OnCountMatchAnswerChange(countMatchAnswer = answer )
                                    )
                                },
                                selectedCount = state.countMatchAnswer,
                                onSubmitAddition = {

                                    onAction(
                                        NumeracyAssessmentAction.OnAddArithmeticOperation(
                                            numeracyOperations = NumeracyOperations(
                                                firstNumber = state.numeracyAssessmentContent.data.additions[state.additionIndex].firstNumber,
                                                secondNumber = state.numeracyAssessmentContent.data.additions[state.additionIndex].secondNumber,
                                                answer = state.numeracyAssessmentContent.data.additions[state.additionIndex].answer,
                                                operationType = state.numeracyAssessmentContent.data.additions[state.additionIndex].operationType
                                            ),
                                            onSuccess = {
                                                when {
                                                    state.additionIndex + 1 >= state.numeracyAssessmentContent.data.additions.size -> {
                                                        onAction(NumeracyAssessmentAction.OnSubmitNumeracyOperations(
                                                            operationList = state.arithmeticOperationResults,
                                                            assessmentId = assessmentId,
                                                            studentId = studentId,
                                                            onSuccess = {
                                                                onAction(NumeracyAssessmentAction.OnNumeracyLevelChange(NumeracyAssessmentLevel.SUBTRACTION))
                                                            }
                                                        ))
                                                    }
                                                    else -> {
                                                        onAction(NumeracyAssessmentAction.OnAdditionIndexChange(index = state.additionIndex + 1))
                                                    }
                                                }
                                            }
                                        )
                                    )
                                },
                                additionIndex = state.additionIndex,
                                onSubmitCountMatch = {
                                    onAction(
                                        NumeracyAssessmentAction.OnAddCountMatch(
                                            countMatch = state.numeracyAssessmentContent.data.countAndMatchNumbersList[state.countMatchIndex],
                                            onSuccess = {
                                                onAction(
                                                    NumeracyAssessmentAction.OnSubmitCountMatch(
                                                        countMatch = state.countAndMatchResults,
                                                        assessmentId = assessmentId,
                                                        studentId = studentId,
                                                        onSuccess = {
                                                            when {
                                                                state.countMatchIndex + 1 >= state.numeracyAssessmentContent.data.countAndMatchNumbersList.size -> {
                                                                    onAction(NumeracyAssessmentAction.OnNumeracyLevelChange(NumeracyAssessmentLevel.ADDITION))
                                                                }
                                                                else -> {
                                                                    onAction(NumeracyAssessmentAction.OnCountMachIndexChange(index = state.countMatchIndex + 1))
                                                                }
                                                            }
                                                        }
                                                    )
                                                )
                                            }
                                        )
                                    )
                                },
                                onSubmitSubtraction = {
                                    onAction(
                                        NumeracyAssessmentAction.OnAddArithmeticOperation(
                                            numeracyOperations = NumeracyOperations(
                                                firstNumber = state.numeracyAssessmentContent.data.subtractions[state.subtractionIndex].firstNumber,
                                                secondNumber = state.numeracyAssessmentContent.data.subtractions[state.subtractionIndex].secondNumber,
                                                answer = state.numeracyAssessmentContent.data.subtractions[state.subtractionIndex].answer,
                                                operationType = state.numeracyAssessmentContent.data.subtractions[state.subtractionIndex].operationType
                                            ),
                                            onSuccess = {
                                                when {
                                                    state.subtractionIndex + 1 >= state.numeracyAssessmentContent.data.subtractions.size -> {
                                                        onAction(NumeracyAssessmentAction.OnSubmitNumeracyOperations(
                                                            operationList = state.arithmeticOperationResults,
                                                            assessmentId = assessmentId,
                                                            studentId = studentId,
                                                            onSuccess = {
                                                                onAction(NumeracyAssessmentAction.OnNumeracyLevelChange(NumeracyAssessmentLevel.MULTIPLICATION))
                                                            }
                                                        ))
                                                    }
                                                    else -> {
                                                        onAction(NumeracyAssessmentAction.OnSubtractionIndexChange(index = state.subtractionIndex + 1))
                                                    }
                                                }
                                            }
                                        )
                                    )

                                },
                                onSubmitMultiplication = {
                                    onAction(
                                        NumeracyAssessmentAction.OnAddArithmeticOperation(
                                            numeracyOperations = NumeracyOperations(
                                                firstNumber = state.numeracyAssessmentContent.data.multiplications[state.multiplicationIndex].firstNumber,
                                                secondNumber = state.numeracyAssessmentContent.data.multiplications[state.multiplicationIndex].secondNumber,
                                                answer = state.numeracyAssessmentContent.data.multiplications[state.multiplicationIndex].answer,
                                                operationType = state.numeracyAssessmentContent.data.multiplications[state.multiplicationIndex].operationType
                                            ),
                                            onSuccess = {
                                                when {
                                                    state.multiplicationIndex + 1 >= state.numeracyAssessmentContent.data.multiplications.size -> {
                                                        onAction(NumeracyAssessmentAction.OnSubmitNumeracyOperations(
                                                            operationList = state.arithmeticOperationResults,
                                                            assessmentId = assessmentId,
                                                            studentId = studentId,
                                                            onSuccess = {
                                                                onAction(NumeracyAssessmentAction.OnNumeracyLevelChange(NumeracyAssessmentLevel.DIVISION))
                                                            }
                                                        ))
                                                    }
                                                    else -> {
                                                        onAction(NumeracyAssessmentAction.OnMultiplicationIndexChange(index = state.multiplicationIndex + 1))
                                                    }
                                                }
                                            }
                                        )
                                    )

                                },
                                onSubmitWordProblem = {
                                    /*
                                    onAction(
                                        NumeracyAssessmentAction.OnSubmitWordProblem(
                                            wordProblem = NumeracyWordProblem(

                                            ),
                                            assessmentId = assessmentId,
                                            studentId = studentId,
                                            onSuccess = {
                                                onAction(NumeracyAssessmentAction.OnNumeracyLevelChange(NumeracyAssessmentLevel.NUMBER_RECOGNITION))
                                            }
                                        )
                                    )*/
                                },
                                onSubmitDivision = {
                                    onAction(
                                        NumeracyAssessmentAction.OnAddArithmeticOperation(
                                            numeracyOperations = NumeracyOperations(
                                                firstNumber = state.numeracyAssessmentContent.data.divisions[state.divisionIndex].firstNumber,
                                                secondNumber = state.numeracyAssessmentContent.data.divisions[state.divisionIndex].secondNumber,
                                                answer = state.numeracyAssessmentContent.data.divisions[state.divisionIndex].answer,
                                                operationType = state.numeracyAssessmentContent.data.divisions[state.divisionIndex].operationType
                                            ),
                                            onSuccess = {
                                                when {
                                                    state.divisionIndex + 1 >= state.numeracyAssessmentContent.data.divisions.size -> {
                                                        onAction(NumeracyAssessmentAction.OnSubmitNumeracyOperations(
                                                            operationList = state.arithmeticOperationResults,
                                                            assessmentId = assessmentId,
                                                            studentId = studentId,
                                                            onSuccess = {
                                                                onAction(NumeracyAssessmentAction.OnNumeracyLevelChange(NumeracyAssessmentLevel.WORD_PROBLEM))
                                                            }
                                                        ))
                                                    }
                                                    else -> {
                                                        onAction(NumeracyAssessmentAction.OnDivisionIndexChange(index = state.divisionIndex + 1))
                                                    }
                                                }
                                            }
                                        )
                                    )
                                },
                                onSubmitNumberRecognition = {},
                                responseError = state.responseError,
                                answerResponse = state.response,
                                onCaptureAnswerContent = {image ->
                                    onAction(NumeracyAssessmentAction.OnCaptureAnswer(image))

                                },
                                shouldCaptureAnswer = state.shouldCaptureAnswer,
                                onCaptureWorkAreaContent = { image ->
                                    onAction(NumeracyAssessmentAction.OnCaptureWorkArea(image))
                                },
                                showResponseAlert = state.showResponseAlert,
                                onDismissResponseAlert = {
                                    onAction(NumeracyAssessmentAction.OnShowResponseAlertChange(false))
                                }
                            )

                        }

                        /*
                        item {
                            NumeracyOperation(
                                firstNumber = 34,
                                secondNumber = 20,
                                shouldCaptureAnswer = state.shouldCaptureAnswer,
                                shouldCaptureWorkArea = state.shouldCaptureWorkArea,
                                onCaptureAnswerContent = { imageByteArray ->
                                    onAction(NumeracyAssessmentAction.OnCaptureAnswer(imageByteArray))
                                },
                                onCaptureWorkAreaContent = { imageByteArray ->
                                    onAction(NumeracyAssessmentAction.OnCaptureWorkArea(imageByteArray))
                                },
                                onSubmit = {
                                    onAction(NumeracyAssessmentAction.OnSubmitAnswer)
                                },
                                modifier = modifier
                            )
                        }*/
                    }

                }
            }
        }

         */

    }



}