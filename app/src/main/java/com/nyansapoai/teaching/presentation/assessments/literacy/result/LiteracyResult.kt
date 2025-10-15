package com.nyansapoai.teaching.presentation.assessments.literacy.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.CharResultItem
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.ComprehensionQuestion
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.ParagraphResultItem
import com.nyansapoai.teaching.presentation.common.audio.AppNetworkAudioPlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyResultRoot(
    modifier: Modifier = Modifier,
    assessmentId: String,
    studentId: String,
) {

    val viewModel = koinViewModel<LiteracyResultViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LiteracyResultScreen(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiteracyResultScreen(
    modifier: Modifier = Modifier,
    state: LiteracyResultState,
    onAction: (LiteracyResultAction) -> Unit,
) {

    if (state.selectedAudioUrl != null){
        ModalBottomSheet(
            onDismissRequest = {
                onAction.invoke(LiteracyResultAction.OnSelectAudioUrl(audioUrl = null))
            }
        ) {
            AppNetworkAudioPlayer(
                audioUrl = state.selectedAudioUrl,
            )
        }
    }

    LazyColumn(
        modifier = modifier
    )
    {

        if (state.letters.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Letters"
                )
            }

            item {
                FlowRow(

                ) {
                    state.letters.forEach { result ->
                        CharResultItem(
                            char = result.content,
                            isCorrect = result.metadata?.passed ?: false,
                            onClick = {
                                onAction.invoke(LiteracyResultAction.OnSelectAudioUrl(audioUrl = result.metadata?.audio_url))
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

        }


        if (state.words.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Words"
                )
            }

            item {
                FlowRow(

                ) {
                    state.words.forEach { result ->
                        CharResultItem(
                            char = result.content,
                            isCorrect = result.metadata?.passed ?: false,
                            onClick = {
                                onAction.invoke(LiteracyResultAction.OnSelectAudioUrl(audioUrl = result.metadata?.audio_url))
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

        }

        if (state.paragraphs.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Paragraph"
                )
            }

            items(items = state.paragraphs) { result ->
                ParagraphResultItem(
                    expected = result.content,
                    studentAnswer = result.metadata?.transcript ?: "",
                    onClick = {
                        onAction.invoke(LiteracyResultAction.OnSelectAudioUrl(audioUrl = result.metadata?.audio_url))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

        }

        if (state.stories.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Story"
                )
            }

            items(items = state.stories){ result ->
                ParagraphResultItem(
                    expected = result.content,
                    studentAnswer = result.metadata?.transcript ?: "",
                    onClick = {
                        onAction.invoke(LiteracyResultAction.OnSelectAudioUrl(audioUrl = result.metadata?.audio_url))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

        }

        if (state.multipleChoiceQuestions.isNotEmpty()){
            stickyHeader {
                TitleText(
                    text = "Multiple Choice Questions"
                )
            }

            items(items = state.multipleChoiceQuestions) { result ->
                ComprehensionQuestion(
                    result = result,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }
}


@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
    }

}