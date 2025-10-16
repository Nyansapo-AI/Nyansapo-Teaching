package com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.ImageResult
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.composable.ReviewAssessmentAudio
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.composable.ReviewAssessmentImage
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult.TitleText
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult.components.CharResultItem
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults.composables.NumeracyOperationResultItemUI
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults.composables.NumeracyWordProblemResultItemUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun NumeracyAssessmentResultRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
) {

    val viewModel = koinViewModel<NumeracyAssessmentResultViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumeracyAssessmentResultScreen(
        modifier = modifier,
        state = state,
        studentId = studentId,
        assessmentId = assessmentId,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumeracyAssessmentResultScreen(
    modifier: Modifier = Modifier,
    state: NumeracyAssessmentResultState,
    studentId: String,
    assessmentId: String,
    onAction: (NumeracyAssessmentResultAction) -> Unit,
) {


    if (state.selectedImageResult != null){
        ModalBottomSheet(
            onDismissRequest = {
                onAction.invoke(NumeracyAssessmentResultAction.OnSelectImageResult(imageResult = null))
            }
        ) {
            ReviewAssessmentImage(
                imageUrl = state.selectedImageResult.imageUrl,
                studentAnswer = state.selectedImageResult.studentAnswer,
                expectedAnswer = state.selectedImageResult.expectedAnswer,
                transcript = state.selectedImageResult.transcript,
                passed = state.selectedImageResult.passed,
            )
        }
    }

    if (state.selectedNumberRecognition != null){
        ModalBottomSheet(
            onDismissRequest = {
                onAction.invoke(NumeracyAssessmentResultAction.OnSelectedNumeracyRecognitionResult(numberRecognition = null))
            }
        ) {
            ReviewAssessmentAudio(
                audioUrl = state.selectedNumberRecognition.metadata.audio_url,
                studentAnswer = state.selectedNumberRecognition.metadata.transcript ?: "No answer",
                expectedAnswer = state.selectedNumberRecognition.content,
                transcript = state.selectedNumberRecognition.metadata.transcript ?: "No transcript",
                passed = state.selectedNumberRecognition.metadata.passed == true,
            )
        }
    }



    LazyColumn(
        modifier = modifier,
    ) {

        if (state.count_and_match.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Count and Match"
                )
            }

            item {
                FlowRow{
                    state.count_and_match.forEach { result ->
                        CharResultItem(
                            char = result.expected_number.toString(),
                            isCorrect = result.passed == true,
                            onClick = {

                            }
                        )
                    }
                }
            }
        }

        if (state.number_recognition.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Number Recognition"
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.number_recognition.forEach { result ->
                        CharResultItem(
                            char = result.content,
                            isCorrect = result.metadata.passed == true,
                            onClick = {
                                onAction.invoke(NumeracyAssessmentResultAction.OnSelectedNumeracyRecognitionResult(numberRecognition = result))
                            }
                        )
                    }
                }
            }
        }


        if (state.additions.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Additions"
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.additions.forEach { result ->
                        NumeracyOperationResultItemUI(
                            result = result,
                            onClick = {
                                onAction.invoke(
                                    NumeracyAssessmentResultAction.OnSelectImageResult(
                                        imageResult = ImageResult(
                                            studentId = studentId,
                                            assessmentId = assessmentId,
                                            imageUrl = result.metadata.screenshot_url,
                                            studentAnswer = result.student_answer ?: "Undefined",
                                            expectedAnswer = result.expected_answer ?: "Undefined",
                                            transcript = result.metadata.transcript ?: "Undefined",
                                            passed = result.metadata.passed == true
                                        )
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        if (state.subtractions.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Subtractions"
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.subtractions.forEach { result ->
                        NumeracyOperationResultItemUI(
                            result = result,
                            onClick = {
                                onAction.invoke(
                                    NumeracyAssessmentResultAction.OnSelectImageResult(
                                        imageResult = ImageResult(
                                            studentId = studentId,
                                            assessmentId = assessmentId,
                                            imageUrl = result.metadata.screenshot_url,
                                            studentAnswer = result.student_answer ?: "Undefined",
                                            expectedAnswer = result.expected_answer ?: "Undefined",
                                            transcript = result.metadata.transcript ?: "Undefined",
                                            passed = result.metadata.passed == true
                                        )
                                    )
                                )

                            }
                        )
                    }
                }
            }
        }

        if (state.multiplications.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Multiplications"
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.multiplications.forEach { result ->
                        NumeracyOperationResultItemUI(
                            result = result,
                            onClick = {
                                onAction.invoke(
                                    NumeracyAssessmentResultAction.OnSelectImageResult(
                                        imageResult = ImageResult(
                                            studentId = studentId,
                                            assessmentId = assessmentId,
                                            imageUrl = result.metadata.screenshot_url,
                                            studentAnswer = result.student_answer ?: "Undefined",
                                            expectedAnswer = result.expected_answer ?: "Undefined",
                                            transcript = result.metadata.transcript ?: "Undefined",
                                            passed = result.metadata.passed == true
                                        )
                                    )
                                )

                            }
                        )
                    }
                }
            }
        }

        if (state.divisions.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Divisions"
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.divisions.forEach { result ->
                        NumeracyOperationResultItemUI(
                            result = result,
                            onClick = {
                                onAction.invoke(
                                    NumeracyAssessmentResultAction.OnSelectImageResult(
                                        imageResult = ImageResult(
                                            studentId = studentId,
                                            assessmentId = assessmentId,
                                            imageUrl = result.metadata.screenshot_url,
                                            studentAnswer = result.student_answer ?: "Undefined",
                                            expectedAnswer = result.expected_answer ?: "Undefined",
                                            transcript = result.metadata.transcript ?: "Undefined",
                                            passed = result.metadata.passed == true
                                        )
                                    )
                                )

                            }
                        )
                    }
                }
            }
        }

        if (state.word_problem.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Word Problems"
                )
            }

            items(state.word_problem) { result ->
                NumeracyWordProblemResultItemUI(
                    result = result,
                    onClick = {
                        onAction.invoke(
                            NumeracyAssessmentResultAction.OnSelectImageResult(
                                imageResult = ImageResult(
                                    studentId = studentId,
                                    assessmentId = assessmentId,
                                    imageUrl = result.metadata.screenshot_url,
                                    studentAnswer = result.metadata.student_answer ?: "Undefined",
                                    expectedAnswer = result.expected_number,
                                    transcript = result.metadata.transcript ?: "Undefined",
                                    passed = result.metadata.passed == true
                                )
                            )
                        )

                    }
                )
            }
        }
    }

}