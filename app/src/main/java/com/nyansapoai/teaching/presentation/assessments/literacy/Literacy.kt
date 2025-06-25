package com.nyansapoai.teaching.presentation.assessments.literacy

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyLettersRecognitionUI
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
        onAction = viewModel::onAction
    )
}

@Composable
fun LiteracyScreen(
    state: LiteracyState,
    onAction: (LiteracyAction) -> Unit = {},
) {
    LaunchedEffect(state) {
        Log.d("Literacy State", "$state")
    }

    var showAppIntro by remember {
        mutableStateOf(true)
    }

    LiteracyLettersRecognitionUI(
        modifier = Modifier,
        letters = listOf("book", "coat", "sun", "desk", "fish", "ball", "tea", "rat", "house"),
        currentIndex = state.currentIndex,
        showInstructions = state.showInstructions,
        onShowInstructionsChange = {
            onAction(LiteracyAction.SetShowInstructions(it))
        },
        title = "Literacy Assessment",
        fontSize = 60.sp,
        showContent = state.showContent,
        onShowContentChange = {
            onAction(LiteracyAction.SetShowContent(it))
        },
        audioByteArray = state.audioByteArray,
        onAudioByteArrayChange = {
            onAction(LiteracyAction.SetAudioByteArray(it))
        },
        response = state.response,
        onClick = {}
    )


}