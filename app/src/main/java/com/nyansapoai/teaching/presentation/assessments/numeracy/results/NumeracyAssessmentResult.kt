package com.nyansapoai.teaching.presentation.assessments.numeracy.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nyansapoai.teaching.presentation.assessments.literacy.result.TitleText
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.CharResultItem
import com.nyansapoai.teaching.presentation.assessments.numeracy.results.composables.NumeracyOperationResultItemUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.results.composables.NumeracyWordProblemResultItemUI
import com.nyansapoai.teaching.presentation.common.audio.AndroidNetworkAudioPlayer
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
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumeracyAssessmentResultScreen(
    modifier: Modifier = Modifier,
    state: NumeracyAssessmentResultState,
    onAction: (NumeracyAssessmentResultAction) -> Unit,
) {


    if (state.screenshotImage != null){
        ModalBottomSheet(
            onDismissRequest = {
                onAction.invoke(NumeracyAssessmentResultAction.OnSelectScreenshotImage(imageUrl = null))
            }
        ) {
            AsyncImage(
                model = state.screenshotImage,
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }
    }

    if (state.audioUrl != null){
        ModalBottomSheet(
            onDismissRequest = {
                onAction.invoke(NumeracyAssessmentResultAction.OnSelectAudioUrl(audioUrl = null))
            }
        ) {
            AndroidNetworkAudioPlayer(
                audioUrl = state.audioUrl
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
                                onAction.invoke(NumeracyAssessmentResultAction.OnSelectAudioUrl(audioUrl = result.metadata.audio_url))
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
                                onAction.invoke(NumeracyAssessmentResultAction.OnSelectScreenshotImage(imageUrl = result.metadata.screenshot_url))
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
                                onAction.invoke(NumeracyAssessmentResultAction.OnSelectScreenshotImage(imageUrl = result.metadata.screenshot_url))
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
                                onAction.invoke(NumeracyAssessmentResultAction.OnSelectScreenshotImage(imageUrl = result.metadata.screenshot_url))
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
                                onAction.invoke(NumeracyAssessmentResultAction.OnSelectScreenshotImage(imageUrl = result.metadata.screenshot_url))
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
                        onAction.invoke(NumeracyAssessmentResultAction.OnSelectScreenshotImage(imageUrl = result.metadata.screenshot_url))
                    }
                )
            }
        }
    }

}