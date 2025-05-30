package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyAddition
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentRoot() {

    val viewModel = koinViewModel<NumeracyAssessmentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumeracyAssessmentScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NumeracyAssessmentScreen(
    state: NumeracyAssessmentState,
    onAction: (NumeracyAssessmentAction) -> Unit,
) {
    NumeracyAddition(
        firstNumber = 34,
        secondNumber = 20
    )
}