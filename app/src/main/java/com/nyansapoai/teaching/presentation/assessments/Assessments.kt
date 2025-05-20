package com.nyansapoai.teaching.presentation.assessments

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AssessmentsRoot() {

    val viewModel = koinViewModel<AssessmentsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AssessmentsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AssessmentsScreen(
    state: AssessmentsState,
    onAction: (AssessmentsAction) -> Unit,
) {
    LazyColumn {
        item {  }
    }

}