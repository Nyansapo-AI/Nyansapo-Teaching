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
        onAction = viewModel::onAction
    )

}

@Composable
fun ConductAssessmentScreen(
    state: ConductAssessmentState,
    onAction: (ConductAssessmentAction) -> Unit,
) {
    Scaffold(

    ) { innerPadding ->


        /*
        var answerImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var workAreaImage by remember { mutableStateOf<ImageBitmap?>(null) }

        var shouldCaptureAnswer by remember { mutableStateOf(false) }
        var shouldCaptureWorkArea by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                NumeracyOperation(
                    firstNumber = 10,
                    secondNumber = 5,
                    shouldCaptureAnswer = shouldCaptureAnswer,
                    shouldCaptureWorkArea = shouldCaptureWorkArea,
                    onCaptureAnswerContent = { imageBitmap ->
                        answerImage = imageBitmap
                    },
                    onCaptureWorkAreaContent = {
                        workAreaImage = it
                    },
                )

            }
        } */

        NumeracyAssessmentRoot(

        )

    }

}