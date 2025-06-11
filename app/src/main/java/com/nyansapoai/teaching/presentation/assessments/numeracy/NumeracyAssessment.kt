package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.domain.models.assessments.numeracy.numeracyAssessmentData
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyContent
import com.nyansapoai.teaching.presentation.common.animations.LottieLoading
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
) {

    val viewModel = koinViewModel<NumeracyAssessmentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val answerAssessmentState by viewModel.answerImageByteArrayState.collectAsState()
    val workAreaState by viewModel.workAreaImageByteArrayState.collectAsState()

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


    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        delay(8000)
        isLoading = false
    }

    Scaffold { innerPadding  ->

        AnimatedContent(
            targetState = isLoading
        ) { loading ->
            when(loading) {
                true -> {
                    LottieLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
                false -> {

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        /*
                        item {
                            AnimatedContent(
                                targetState = state.numeracyAssessmentContent.status
                            ) { status ->
                                when(status){
                                    ResultStatus.INITIAL ,
                                    ResultStatus.LOADING -> {
                                        AppCircularLoading()
                                    }
                                    ResultStatus.SUCCESS -> {
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

                                    }
                                    ResultStatus.ERROR -> TODO()
                                }
                            }
                        }*/

                        item {
                            state.numeracyAssessmentContent.data?.let {
                                NumeracyContent(
                                    modifier = modifier,
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
                                    onSubmitSubtraction = {},
                                    onSubmitMultiplication = {},
                                    onSubmitWordProblem = {},
                                    onSubmitDivision = {},
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

    }



}