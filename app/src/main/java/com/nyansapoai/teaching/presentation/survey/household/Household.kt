package com.nyansapoai.teaching.presentation.survey.household

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HouseholdRoot() {

    val viewModel = koinViewModel<HouseholdViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HouseholdScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HouseholdScreen(
    state: HouseholdState,
    onAction: (HouseholdAction) -> Unit,
) {

}