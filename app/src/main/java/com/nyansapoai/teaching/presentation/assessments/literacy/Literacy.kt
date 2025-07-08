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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.components.HasCompletedAssessment
import com.nyansapoai.teaching.presentation.assessments.literacy.LiteracyAction.*
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyReadingAssessmentUI
import com.nyansapoai.teaching.presentation.assessments.literacy.components.MultichoiceQuestionsUI
import com.nyansapoai.teaching.presentation.assessments.literacy.components.ReadingStoryEvaluationUI
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
    assessmentNo: Int
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
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiteracyScreen(
    state: LiteracyState,
    assessmentId: String,
    studentId: String,
    onAction: (LiteracyAction) -> Unit = {},
) {

    LaunchedEffect(true) {
        onAction(LiteracyAction.SetIds(assessmentId = assessmentId, studentId = studentId))
    }

    LaunchedEffect(state.currentAssessmentLevel == LiteracyAssessmentLevel.COMPLETED) {
        onAction.invoke(LiteracyAction.OnSubmitLiteracyResults(assessmentId = assessmentId, studentId = studentId))
    }

    Scaffold(
        
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            /*
            LazyColumn {
                item {
                }
            }*/

            AppSimulateNavigation(
                modifier = Modifier,
                targetState = state.currentAssessmentLevel
            ) {
                when (state.currentAssessmentLevel) {
                    LiteracyAssessmentLevel.LETTER_RECOGNITION -> {
                        LiteracyReadingAssessmentUI(
                            modifier = Modifier,
                            readingList = state.assessmentContent?.letters ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(SetShowInstructions(it))
                            },
                            title = "Letter",
                            fontSize = 120.sp,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onAudioPathChange = {
                                onAction(SetAudioFilePath(audioFilePath = it))
                            },
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
                            readingList = state.assessmentContent?.words ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(SetShowInstructions(it))
                            },
                            title = "Words",
                            instructionTitle = "Read the word",
                            fontSize = 80.sp,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onAudioPathChange = {
                                onAction(SetAudioFilePath(audioFilePath = it))
                            },
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
                        LiteracyReadingAssessmentUI(
                            modifier = Modifier,
                            readingList = state.assessmentContent?.paragraphs ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(SetShowInstructions(it))
                            },
                            title = "Paragraphs",
                            instructionTitle = "Read the paragraph",
                            fontSize = 40.sp,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onAudioPathChange = {
                                onAction(SetAudioFilePath(audioFilePath = it))
                            },
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

                    LiteracyAssessmentLevel.STORY -> {

                        LiteracyReadingAssessmentUI(
                            modifier = Modifier,
                            readingList = state.assessmentContent?.storys[0]?.split(".") ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(SetShowInstructions(it))
                            },
                            title = "Reading Story",
                            instructionTitle = "Read the sentence",
                            fontSize = 40.sp,
                            showQuestionNumber = true,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onAudioPathChange = {
                                onAction(SetAudioFilePath(audioFilePath = it))
                            },
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

                    LiteracyAssessmentLevel.MULTIPLE_CHOICE -> {
                        MultichoiceQuestionsUI(
                            currentIndex = state.currentIndex,
                            story = state.assessmentContent?.storys[0] ?: "",
                            questionsList = state.assessmentContent?.questionsData ?: emptyList(),
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