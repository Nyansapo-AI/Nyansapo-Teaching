package com.nyansapoai.teaching.presentation.assessments.literacy.result

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.CharResultItem
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.ParagraphResultItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyResultRoot(
    assessmentId: String,
    studentId: String,
) {

    val viewModel = koinViewModel<LiteracyResultViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LiteracyResultScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LiteracyResultScreen(
    state: LiteracyResultState,
    onAction: (LiteracyResultAction) -> Unit,
) {

    LazyColumn {

        stickyHeader {
            Text("Letter")
        }

        items(items = state.letters){ result ->
            CharResultItem(
                char = result.content,
                isCorrect = result.metadata?.passed ?: false
            )
        }

        stickyHeader {
            Text(text = "Words")
        }

        item {
            FlowRow(

            ) {
                state.words.forEach { result ->
                    CharResultItem(
                        char = result.content,
                        isCorrect = result.metadata?.passed ?: false
                    )
                }
            }
        }


        stickyHeader {
            Text(text = "Paragraphs")
        }

        items(items = state.paragraphs) { result ->
//            Text(text = result.content)
            ParagraphResultItem(
                expected = result.content,
                studentAnswer = result.metadata?.transcript ?: ""
            )
        }
    }

    
}