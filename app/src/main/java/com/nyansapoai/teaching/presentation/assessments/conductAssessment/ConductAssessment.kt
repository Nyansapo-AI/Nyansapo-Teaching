package com.nyansapoai.teaching.presentation.assessments.conductAssessment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.literacy.LiteracyRoot
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConductAssessmentRoot(
    assessmentId: String,
    assessmentType: String,
    studentId: String,
    assessmentNo: Int
) {

    val viewModel = koinViewModel<ConductAssessmentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ConductAssessmentScreen(
        state = state,
        onAction = viewModel::onAction,
        assessmentId = assessmentId,
        studentId = studentId,
        assessmentType = assessmentType,
        assessmentNo = assessmentNo
    )

}

@Composable
fun ConductAssessmentScreen(
    assessmentId: String,
    studentId: String,
    assessmentType: String,
    assessmentNo: Int,
    state: ConductAssessmentState,
    onAction: (ConductAssessmentAction) -> Unit,
) {
    when(assessmentType){
        "Literacy" -> {
            LiteracyRoot(
                assessmentId = assessmentId,
                studentId = studentId,
                assessmentNo = assessmentNo
            )
        }
        "Numeracy" -> {
            NumeracyAssessmentRoot(
                assessmentId = assessmentId,
                studentId = studentId,
                assessmentNo = assessmentNo
            )
        }
        else -> {
            // Handle unknown assessment type
            // You can show an error message or navigate to a default screen
        }
    }



}