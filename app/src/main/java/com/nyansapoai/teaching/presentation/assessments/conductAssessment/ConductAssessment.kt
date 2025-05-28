package com.nyansapoai.teaching.presentation.assessments.conductAssessment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.common.components.AppTouchInput
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConductAssessmentRoot(
    assessmentId: String,
    studentId: String
) {

    val viewModel = koinViewModel<ConductAssessmentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ConductAssessmentScreen(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun ConductAssessmentScreen(
    state: ConductAssessmentState,
    onAction: (ConductAssessmentAction) -> Unit,
) {
    AppTouchInput()
}