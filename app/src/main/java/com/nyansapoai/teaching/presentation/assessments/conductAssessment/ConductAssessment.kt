package com.nyansapoai.teaching.presentation.assessments.conductAssessment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentRoot
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
        onAction = viewModel::onAction,
        assessmentId = assessmentId,
        studentId = studentId,
    )

}

@Composable
fun ConductAssessmentScreen(
    assessmentId: String,
    studentId: String,
    state: ConductAssessmentState,
    onAction: (ConductAssessmentAction) -> Unit,
) {
    NumeracyAssessmentRoot(
        assessmentId = assessmentId,
        studentId = studentId,
    )


}