package com.nyansapoai.teaching.presentation.camps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun CampRoot() {

    val viewModel = koinViewModel<CampViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CampScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CampScreen(
    state: CampState,
    onAction: (CampAction) -> Unit,
) {

}