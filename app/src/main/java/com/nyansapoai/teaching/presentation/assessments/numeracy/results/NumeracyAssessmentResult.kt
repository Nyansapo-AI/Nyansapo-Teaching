package com.nyansapoai.teaching.presentation.assessments.numeracy.results

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentResultRoot() {

    val viewModel = koinViewModel<NumeracyAssessmentResultViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumeracyAssessmentResultScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NumeracyAssessmentResultScreen(
    state: NumeracyAssessmentResultState,
    onAction: (NumeracyAssessmentResultAction) -> Unit,
) {

}