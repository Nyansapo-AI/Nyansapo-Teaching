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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.components.HasCompletedAssessment
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyCountAndMatch
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyOperationContainerUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyWordProblemContainer
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation
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

    LaunchedEffect(state) {
        println("NumeracyAssessmentScreen State: ${state.shouldCaptureAnswer}")
    }

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


        if (state.hasCompletedAssessment){
            HasCompletedAssessment()
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
                        onIsSubmittingChange = {
                            onAction.invoke(
                                NumeracyAssessmentAction.OnIsSubmittingChange(isSubmitting = it)
                            )
                        },
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
                NumeracyAssessmentLevel.WORD_PROBLEM -> {
                    val wordProblemList = state.numeracyAssessmentContent.wordProblems.map { it.problem }

                    NumeracyWordProblemContainer(
                        wordProblemList = wordProblemList,
                        currentIndex = state.currentIndex,
                        onAnswerFilePathChange = { path ->
                            onAction.invoke(
                                NumeracyAssessmentAction.OnAnswerImageFilePathChange(path = path)
                            )
                        },
                        onWorkOutFilePathChange = { path ->
                            onAction.invoke(
                                NumeracyAssessmentAction.OnWorkAreaImageFilePathChange(path = path)
                            )
                        },
                        shouldCapture = state.shouldCaptureAnswer,
                        onIsSubmittingChange = {
                            onAction.invoke(
                                NumeracyAssessmentAction.OnIsSubmittingChange(isSubmitting = it)
                            )
                        },
                        onSubmit = {
                            onAction.invoke(
                                NumeracyAssessmentAction.OnSubmitWordProblem(
                                    assessmentId = assessmentId,
                                    studentId = studentId
                                ) {}
                            )
                        }
                    )
                }
            }

        }

    }



}