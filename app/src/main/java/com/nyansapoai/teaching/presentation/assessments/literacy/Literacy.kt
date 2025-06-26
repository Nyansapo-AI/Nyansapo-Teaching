package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyAssessmentLevel
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyReadingAssessmentUI
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
) {

    val viewModel = koinViewModel<LiteracyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

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

    Scaffold(
        /*
        topBar = {
            MediumTopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = "exit assessment"
                        )
                    }
                }
            )
        }*/
    ) { innerPadding ->
        Box(
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
                            letters = state.assessmentContent?.letters ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(LiteracyAction.SetShowInstructions(it))
                            },
                            title = "Letter",
                            fontSize = 120.sp,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(LiteracyAction.SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(LiteracyAction.SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onSubmit = {
                                onAction(
                                    LiteracyAction.OnSubmitResponse(
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
                            letters = state.assessmentContent?.words ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(LiteracyAction.SetShowInstructions(it))
                            },
                            title = "Words",
                            fontSize = 80.sp,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(LiteracyAction.SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(LiteracyAction.SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onSubmit = {
                                onAction(
                                    LiteracyAction.OnSubmitResponse(
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
                            letters = state.assessmentContent?.paragraphs ?: emptyList(),
                            currentIndex = state.currentIndex,
                            showInstructions = state.showInstructions,
                            onShowInstructionsChange = {
                                onAction(LiteracyAction.SetShowInstructions(it))
                            },
                            title = "Paragraphs",
                            fontSize = 40.sp,
                            showContent = state.showContent,
                            onShowContentChange = {
                                onAction(LiteracyAction.SetShowContent(it))
                            },
                            audioByteArray = state.audioByteArray,
                            onAudioByteArrayChange = {
                                onAction(LiteracyAction.SetAudioByteArray(it))
                            },
                            response = state.response,
                            isLoading = state.isLoading,
                            onSubmit = {
                                onAction(
                                    LiteracyAction.OnSubmitResponse(
                                        assessmentId = assessmentId,
                                        studentId = studentId
                                    )
                                )
                            }
                        )

                    }

                    LiteracyAssessmentLevel.STORY -> {

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

        }

    }



}