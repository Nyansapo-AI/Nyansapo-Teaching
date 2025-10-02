package com.nyansapoai.teaching.presentation.attendances

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AttendancesRoot() {

    val viewModel = koinViewModel<AttendancesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AttendancesScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AttendancesScreen(
    state: AttendancesState,
    onAction: (AttendancesAction) -> Unit,
) {

}