package com.nyansapoai.teaching.presentation.survey.detailedHouseHold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.survey.detailedHouseHold.composables.HouseholdDetailScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailedHouseholdInfoRoot(
    householdId: String,
) {

    val viewModel = koinViewModel<DetailedHouseholdInfoViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailedHouseholdInfoScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun DetailedHouseholdInfoScreen(
    state: DetailedHouseholdInfoState,
    onAction: (DetailedHouseholdInfoAction) -> Unit,
) {

    state.householdInfo?.let {
        HouseholdDetailScreen(
            household = state.householdInfo
        )
    }

}