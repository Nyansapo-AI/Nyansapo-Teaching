package com.nyansapoai.teaching.presentation.common.textToSpeech

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TextToSpeechRoot() {

    val viewModel = koinViewModel<TextToSpeechViewModel>()
//    val state by viewModel.state.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TextToSpeechScreen(
        state = state,
//        onAction = viewModel::onAction
    )
}

@Composable
fun TextToSpeechScreen(
    state: TextToSpeechState,
//    onAction: (TextToSpeechAction) -> Unit,
) {

}