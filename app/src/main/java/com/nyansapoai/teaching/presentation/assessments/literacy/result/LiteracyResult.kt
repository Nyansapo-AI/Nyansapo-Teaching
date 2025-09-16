package com.nyansapoai.teaching.presentation.assessments.literacy.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyResultRoot() {

    val viewModel = koinViewModel<LiteracyResultViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LiteracyResultScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LiteracyResultScreen(
    state: LiteracyResultState,
    onAction: (LiteracyResultAction) -> Unit,
) {
    
}