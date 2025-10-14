package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.assessments.components.HasCompletedAssessment
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyReadingAssessmentUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnAnswerImageFilePathChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnAudioFilePathChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnCountMatchAnswerChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnIsSubmittingChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnShowContentChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnShowInstructionChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnSubmitCountMatch
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnSubmitNumberRecognition
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnSubmitNumeracyOperations
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnSubmitWordProblem
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.OnWorkAreaImageFilePathChange
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction.SubmitNumeracyAssessmentResults
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyArithmeticOperationsContainerUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyCountAndMatch
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyWordProblemContainer
import com.nyansapoai.teaching.presentation.common.components.AppAlertDialog
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
    studentName: String,
    assessmentNo: Int
) {

    val viewModel = koinViewModel<NumeracyAssessmentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumeracyAssessmentScreen(
        state = state,
        onAction = viewModel::onAction,
        studentId = studentId,
        assessmentId = assessmentId,
        studentName = studentName,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumeracyAssessmentScreen(
    modifier: Modifier = Modifier,
    state: NumeracyAssessmentState,
    onAction: (NumeracyAssessmentAction) -> Unit,
    studentId: String,
    assessmentId: String,
    studentName: String
) {

    LaunchedEffect(state.hasCompletedAssessment) {
        if (state.hasCompletedAssessment) {
            onAction(
                SubmitNumeracyAssessmentResults(
                    assessmentId = assessmentId,
                    studentId = studentId
                )
            )

            delay(4000)
            navController.popBackStack()

        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Back",
                        )
                    }
                },
                title = {
                    Text(
                        text = studentName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                    )
                },
                actions = {
                    AnimatedVisibility(
                        visible = !state.hasCompletedAssessment,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            onClick = {
                                onAction.invoke(NumeracyAssessmentAction.OnShowEndAssessmentDialogChange(true))
                            },
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "End",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                            )
                        }

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
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
            HasCompletedAssessment(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )

            return@Scaffold
        }


        AnimatedVisibility(
            visible = state.showEndAssessmentDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppAlertDialog(
                onDismissRequest = { onAction.invoke(NumeracyAssessmentAction.OnShowEndAssessmentDialogChange(false)) },
                dialogText = "You are about to end assessment. Click confirm to continue.",
                dialogTitle = "End Assessment",
                onConfirmation = {
                    onAction.invoke(NumeracyAssessmentAction.OnShowEndAssessmentDialogChange(false))
                    onAction.invoke(NumeracyAssessmentAction.EndAssessment)
                },
            )
        }


        if (state.isLoading) {
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
                .fillMaxSize()
        ){
            when(state.numeracyLevel) {
                NumeracyAssessmentLevel.COUNT_MATCH -> {
                    NumeracyCountAndMatch(
                        countList = state.numeracyAssessmentContent.countAndMatchNumbersList,
                        currentIndex = state.currentIndex,
                        onSelectCount = { count ->
                            onAction(
                                OnCountMatchAnswerChange(countMatchAnswer = count)
                            )
                        },
                        selectedCount = state.countMatchAnswer,
                        onSubmit = {
                            onAction.invoke(
                                OnSubmitCountMatch(
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

                    val title = when(state.numeracyLevel) {
                        NumeracyAssessmentLevel.ADDITION -> "Addition"
                        NumeracyAssessmentLevel.SUBTRACTION -> "Subtraction"
                        NumeracyAssessmentLevel.MULTIPLICATION -> "Multiplication"
                        NumeracyAssessmentLevel.DIVISION -> "Division"
                        else -> ""
                    }

                    NumeracyArithmeticOperationsContainerUI(
                        numeracyOperationList = operationList,
                        currentIndex = state.currentIndex,
                        title = title,
                        onAnswerFilePathChange = {path ->
                            onAction.invoke(
                                OnAnswerImageFilePathChange(path = path)
                            )
                        },
                        onWorkOutFilePathChange = { path ->
                            onAction.invoke(
                                OnWorkAreaImageFilePathChange(path = path)
                            )
                        },
                        isLoading = state.isLoading,
                        shouldCapture = state.shouldCaptureAnswer,
                        onIsSubmittingChange = {
                            onAction.invoke(
                                OnIsSubmittingChange(isSubmitting = it)
                            )
                        },
                        onSubmit = {
                            onAction.invoke(
                                OnSubmitNumeracyOperations(
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
                    LiteracyReadingAssessmentUI(
                        readingList = state.numeracyAssessmentContent.numberRecognitionList.map { it.toString() },
                        currentIndex = state.currentIndex,
                        showInstructions = state.showInstruction,
                        onShowInstructionsChange = { instructionsVisible ->
                            onAction.invoke(OnShowInstructionChange(instructionsVisible))
                        },
                        title = "Read Numbers",
                        showContent = state.showContent,
                        onShowContentChange = { contentVisible ->
                            onAction.invoke(OnShowContentChange(contentVisible))
                        },
                        isLoading = state.isLoading,
                        fontSize = 96.sp,
                        instructionAudio = R.raw.read_number,
                        instructionTitle = "Read the number",
                        instructionDescription = "Read the number shown in the screen",
                        showQuestionNumber = true,
                        onAudioByteArrayChange = {},
                        onAudioPathChange = { filePath ->
                            onAction.invoke(OnAudioFilePathChange(filePath))
                        },
                        audioFilePath = state.audioFilePath,
                        response = null,
                        onSubmit = {
                            onAction.invoke(
                                OnSubmitNumberRecognition(
                                    assessmentId = assessmentId,
                                    studentId = studentId,
                                )
                            )
                        }
                    )
                }
                NumeracyAssessmentLevel.WORD_PROBLEM -> {
                    val wordProblemList = state.numeracyAssessmentContent.wordProblems.map { it.problem }

                    NumeracyWordProblemContainer(
                        wordProblemList = wordProblemList,
                        currentIndex = state.currentIndex,
                        onAnswerFilePathChange = { path ->
                            onAction.invoke(
                                OnAnswerImageFilePathChange(path = path)
                            )
                        },
                        onWorkOutFilePathChange = { path ->
                            onAction.invoke(
                                OnWorkAreaImageFilePathChange(path = path)
                            )
                        },
                        shouldCapture = state.shouldCaptureAnswer,
                        onIsSubmittingChange = {
                            onAction.invoke(
                                OnIsSubmittingChange(isSubmitting = it)
                            )
                        },
                        onSubmit = {
                            onAction.invoke(
                                OnSubmitWordProblem(
                                    assessmentId = assessmentId,
                                    studentId = studentId,
                                    onSuccess = {}
                                )
                            )
                        }
                    )
                }


            }

        }

    }



}