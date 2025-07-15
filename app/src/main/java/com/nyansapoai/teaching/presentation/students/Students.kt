package com.nyansapoai.teaching.presentation.students

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StudentsRoot() {

    val viewModel = koinViewModel<StudentsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    StudentsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun StudentsScreen(
    state: StudentsState,
    onAction: (StudentsAction) -> Unit,
) {

}