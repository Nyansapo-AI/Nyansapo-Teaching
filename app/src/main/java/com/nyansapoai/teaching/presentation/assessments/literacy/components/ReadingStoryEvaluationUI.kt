package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ReadingStoryEvaluationUI(
    modifier: Modifier = Modifier,
    currentIndex: Int = 0,
    showInstructions: Boolean = true,
    showContent: Boolean = false,
    isLoading: Boolean = false,
    audioByteArray: ByteArray? = null,
    response: String? = null,
    onAudioByteArrayChange: (ByteArray) -> Unit = {},
    onShowInstructionsChange: (Boolean) -> Unit = {},
    onShowContentChange: (Boolean) -> Unit = {},
    onSubmit: () -> Unit = {},
    storySentencesList: List<String>
) {

    LiteracyReadingAssessmentUI(
        modifier = modifier,
        readingList = storySentencesList,
        currentIndex = currentIndex,
        showInstructions = showInstructions,
        onShowInstructionsChange = onShowInstructionsChange,
        title = "Reading Story",
        fontSize = 40.sp,
        showContent = showContent,
        showQuestionNumber = false,
        onShowContentChange = onShowContentChange,
        isLoading = isLoading,
        audioByteArray = audioByteArray,
        onAudioByteArrayChange = onAudioByteArrayChange,
        response = response,
        onSubmit = onSubmit
    )

}
