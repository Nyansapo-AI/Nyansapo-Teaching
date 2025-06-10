package com.nyansapoai.teaching.presentation.assessments.conductAssessment

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentRoot
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyOperation
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