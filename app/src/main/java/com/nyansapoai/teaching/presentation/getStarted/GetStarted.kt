package com.nyansapoai.teaching.presentation.getStarted

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel


@Composable
fun GetStartedRoot() {

    val viewModel = koinViewModel<GetStartedViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    GetStartedScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun GetStartedScreen(
    state: GetStartedState,
    onAction: (GetStartedAction) -> Unit,
) {

}