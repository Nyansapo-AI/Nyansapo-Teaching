package com.nyansapoai.teaching.presentation.assessments.literacy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.assessments.components.HasCompletedAssessment
import com.nyansapoai.teaching.presentation.assessments.literacy.LiteracyAction.*
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyReadingAssessmentUI
import com.nyansapoai.teaching.presentation.assessments.literacy.components.MultichoiceQuestionsUI
import com.nyansapoai.teaching.presentation.assessments.literacy.components.PreTestReadingAssessmentUI
import com.nyansapoai.teaching.presentation.assessments.literacy.components.ReadingStoryEvaluationUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentAction
import com.nyansapoai.teaching.presentation.common.components.AppAlertDialog
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.invoke

@Composable
fun LiteracyRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
    assessmentNo: Int,
    studentName: String
) {

    val viewModel = koinViewModel<LiteracyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.fetchAssessmentContent(assessmentNo = assessmentNo)
    }

    LiteracyScreen(
        state = state,
        assessmentId = assessmentId,
        studentId = studentId,
        studentName = studentName,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiteracyScreen(
    modifier: Modifier = Modifier,
    state: LiteracyState,
    assessmentId: String,
    studentId: String,
    studentName: String,
    onAction: (LiteracyAction) -> Unit = {},
) {

    LaunchedEffect(true) {
        onAction(LiteracyAction.SetIds(assessmentId = assessmentId, studentId = studentId))
    }

    LaunchedEffect(state.currentAssessmentLevel) {
        if (state.currentAssessmentLevel == LiteracyAssessmentLevel.COMPLETED) {
            onAction.invoke(OnSubmitLiteracyResults(assessmentId = assessmentId, studentId = studentId))
            delay(3000)
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
                        visible = !state.hasCompletedAssessment && state.currentAssessmentLevel != LiteracyAssessmentLevel.PRE_TEST,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            onClick = {
                                onAction.invoke(LiteracyAction.OnShowEndAssessmentDialogChange(true))
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


    ) { innerPadding ->

        AnimatedVisibility(
            visible = state.showEndAssessmentDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppAlertDialog(
                onDismissRequest = { onAction.invoke(LiteracyAction.OnShowEndAssessmentDialogChange(false)) },
                dialogText = "You are about to end assessment. Click confirm to continue.",
                dialogTitle = "End Assessment",
                onConfirmation = {
                    onAction.invoke(LiteracyAction.OnShowEndAssessmentDialogChange(false))
                    onAction.invoke(LiteracyAction.OnEndAssessment)
                },
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            AppSimulateNavigation(
                modifier = Modifier,
                targetState = state.currentAssessmentLevel
            ) {
                when (state.currentAssessmentLevel) {
                    LiteracyAssessmentLevel.LETTER_RECOGNITION -> {
                        LiteracyReadingAssessmentUI(
                            modifier = Modifier,
                            readingList = state.assessmentContent?.letters?.take(5) ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            showQuestionNumber = false,
                            onShowInstructionsChange = {
                                onAction(SetShowInstructions(it))
                            },
                            title = "Letter Recognition",
                            fontSize = 120.sp,
                            showContent = state.showContent,
                            instructionAudio = R.raw.read_letter,
                            onShowContentChange = {
                                onAction(SetShowContent(it))
                            },
                            onAudioByteArrayChange = {
                                onAction(SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onAudioPathChange = {
                                onAction(SetAudioFilePath(audioFilePath = it))
                            },
                            audioFilePath = state.audioFilePath,
                            onSubmit = {
                                onAction(
                                    OnSubmitResponse(
                                        assessmentId = assessmentId,
                                        studentId = studentId
                                    )
                                )
                            }
                        )

                    }

                    LiteracyAssessmentLevel.WORD -> {
                        LiteracyReadingAssessmentUI(
                            modifier = Modifier,
                            readingList = state.assessmentContent?.words?.take(5) ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(SetShowInstructions(it))
                            },
                            title = "Words",
                            instructionTitle = "Read the word",
                            fontSize = 80.sp,
                            showContent = state.showContent,
                            instructionAudio = R.raw.read_word,
                            onShowContentChange = {
                                onAction(SetShowContent(it))
                            },
                            onAudioByteArrayChange = {
                                onAction(SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onAudioPathChange = {
                                onAction(SetAudioFilePath(audioFilePath = it))
                            },
                            audioFilePath = state.audioFilePath,
                            onSubmit = {
                                onAction(
                                    OnSubmitResponse(
                                        assessmentId = assessmentId,
                                        studentId = studentId
                                    )
                                )
                            }
                        )
                    }

                    LiteracyAssessmentLevel.PARAGRAPH -> {
                        ReadingStoryEvaluationUI(
                            currentIndex = state.currentIndex,
                            title = "Read the Paragraph",
                            storyTitle = null,
                            showInstructions = state.showInstructions,
                            instructionAudio = R.raw.read_sentence,
                            isLoading = state.isLoading,
                            audioFilePath = state.audioFilePath,
                            onShowInstructionsChange = { show -> onAction(SetShowInstructions(show)) },
                            onAudioPathChange = { path -> onAction(SetAudioFilePath(audioFilePath = path)) },
                            onSubmit = {
                                onAction.invoke(
                                    OnSubmitResponse(
                                        assessmentId = assessmentId,
                                        studentId = studentId
                                    )
                                )
                            },
//                            storySentencesList = state.assessmentContent?.paragraphs[0]?.split(".")?: emptyList()
                            storySentencesList = listOf(state.assessmentContent?.paragraphs[0] ?: "")
                        )


                    }

                    LiteracyAssessmentLevel.STORY -> {
                        ReadingStoryEvaluationUI(
                            currentIndex = state.currentIndex,
                            title = "Reading Story",
                            storyTitle = state.assessmentContent?.storys[0]?.title,
                            showInstructions = state.showInstructions,
                            instructionAudio = R.raw.read_sentence,
                            isLoading = state.isLoading,
                            audioFilePath = state.audioFilePath,
                            onShowInstructionsChange = { show -> onAction(SetShowInstructions(show)) },
                            onAudioPathChange = { path -> onAction(SetAudioFilePath(audioFilePath = path)) },
                            onSubmit = {
                                onAction.invoke(
                                    OnSubmitResponse(
                                        assessmentId = assessmentId,
                                        studentId = studentId
                                    )
                                )
                            },
                            storySentencesList = listOf(state.assessmentContent?.storys[0]?.story ?: "")
                        )
                    }

                    LiteracyAssessmentLevel.MULTIPLE_CHOICE -> {
                        MultichoiceQuestionsUI(
                            currentIndex = state.currentIndex,
                            story = state.assessmentContent?.storys[0]?.story  ?: "",
                            questionsList = state.assessmentContent?.storys[0]?.questionsData ?: emptyList(),
                            selectedChoice = state.selectedChoice,
                            onSelectedChoiceChange = {
                                onAction(SetSelectedChoice(it))
                            },
                            onSubmitMultipleChoices = {
                                onAction(
                                    OnSubmitMultipleChoiceResponse(
                                        assessmentId = assessmentId,
                                        studentId = studentId
                                    )
                                )
                            },
                            onSetOptionsList = { options ->
                                onAction(SetMultipleQuestionOptions(options))
                            }
                        )
                    }

                    LiteracyAssessmentLevel.COMPLETED -> {
                        HasCompletedAssessment()
                    }

                    LiteracyAssessmentLevel.PRE_TEST -> {
                        PreTestReadingAssessmentUI(
                            onStart = {
                                onAction(OnCompletePreTest)
                            }
                        )
                    }
                }
            }




            AnimatedVisibility(
                visible = state.isLoading,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)
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

            AnimatedVisibility(
                visible = state.error != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(10))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(10)
                    )
            ) {
                Text(
                    text = state.error ?: "An error occurred. Please try again.",
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .padding(12.dp),
                    fontSize = 16.sp
                )
            }

            AnimatedVisibility(
                visible = state.message != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(10))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(10)
                    )

            ) {
                Text(
                    text = state.message ?: "Message Unknown",
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .padding(12.dp),
                    fontSize = 16.sp
                )

            }

        }

    }



}