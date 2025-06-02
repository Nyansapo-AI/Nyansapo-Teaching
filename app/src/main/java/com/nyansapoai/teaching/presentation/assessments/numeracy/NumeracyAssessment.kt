package com.nyansapoai.teaching.presentation.assessments.numeracy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyOperation
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.utils.ResultStatus
import com.nyansapoai.teaching.utils.Results
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentRoot(
    modifier: Modifier = Modifier
) {

    val viewModel = koinViewModel<NumeracyAssessmentViewModel>()
    val state by viewModel.state.collectAsState()
    val answerAssessmentState by viewModel.answerImageByteArrayState.collectAsState()
    val workAreaState by viewModel.workAreaImageByteArrayState.collectAsState()

    NumeracyAssessmentScreen(
        state = state,
        onAction = viewModel::onAction,
        answerAssessmentState = answerAssessmentState ,
        workAreaState = workAreaState,
        modifier = modifier
    )
}

@Composable
fun NumeracyAssessmentScreen(
    state: NumeracyAssessmentState,
    onAction: (NumeracyAssessmentAction) -> Unit,
    answerAssessmentState: Results<VisionRecognition>,
    workAreaState: Results<VisionRecognition> = Results.initial(),
    modifier: Modifier = Modifier
) {


    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            when(answerAssessmentState.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    AppCircularLoading()
                }
                ResultStatus.SUCCESS -> {
                    Column {
                        Text(
                            text = answerAssessmentState.data?.response.toString(),
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    Column {
                        Text(
                            text = answerAssessmentState.message ?: "An error occurred",
                        )
                    }
                }
            }
        }

        item {
            when(workAreaState.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    AppCircularLoading()
                }
                ResultStatus.SUCCESS -> {
                    Column {
                        Text(
                            text = workAreaState.data?.response.toString(),
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    Column {
                        Text(
                            text = workAreaState.message ?: "An error occurred",
                        )
                    }
                }
            }
        }

        item {
            NumeracyOperation(
                firstNumber = 34,
                secondNumber = 20,
                shouldCaptureAnswer = state.shouldCaptureAnswer,
                shouldCaptureWorkArea = state.shouldCaptureWorkArea,
                onCaptureAnswerContent = { imageByteArray ->
                    onAction(NumeracyAssessmentAction.OnCaptureAnswer(imageByteArray))
                },
                onCaptureWorkAreaContent = { imageByteArray ->
                    onAction(NumeracyAssessmentAction.OnCaptureWorkArea(imageByteArray))
                },
                onSubmit = {
                    onAction(NumeracyAssessmentAction.OnSubmitAnswer)
                },
                modifier = modifier
            )
        }
    }

}