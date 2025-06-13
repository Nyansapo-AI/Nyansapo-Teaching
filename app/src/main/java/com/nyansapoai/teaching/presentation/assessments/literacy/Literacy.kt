package com.nyansapoai.teaching.presentation.assessments.literacy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyRoot() {

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
    onAction: (LiteracyAction) -> Unit,
) {


}