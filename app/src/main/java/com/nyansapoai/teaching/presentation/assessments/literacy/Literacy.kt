package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    LaunchedEffect(state) {
        Log.d("Literacy State", "$state")
    }


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
        AppSimulateNavigation(
            modifier = Modifier
                .padding(innerPadding),
            targetState = state.currentAssessmentLevel
        ){
            when(state.currentAssessmentLevel){
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
                        title = "Words",
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

    }



}