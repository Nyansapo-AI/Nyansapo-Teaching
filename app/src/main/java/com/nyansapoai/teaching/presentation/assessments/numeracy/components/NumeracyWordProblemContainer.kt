package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator

@Composable
fun NumeracyWordProblemContainer(
    modifier: Modifier = Modifier,
    wordProblemList: List<String>,
    currentIndex: Int = 0,
    onAnswerFilePathChange: (String) -> Unit,
    onWorkOutFilePathChange: (String) -> Unit,
    shouldCapture: Boolean,
    onIsSubmittingChange: (Boolean) -> Unit,
    onSubmit: () -> Unit = {  },
) {

    LaunchedEffect(shouldCapture) {
        onSubmit.invoke()
    }

    if (wordProblemList.isEmpty()){
        Text(
            text= "Questions are not available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    if (currentIndex >= wordProblemList.size) {
        Text(
            text = "No more questions available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    var progress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(currentIndex) {
        progress = if (currentIndex < wordProblemList.size) {
            (currentIndex + 1).toFloat() / wordProblemList.size.toFloat()
        } else {
            1f
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .widthIn(max = 700.dp)
            .padding(16.dp),
    ) {
        Text(
            text = "Word Problem",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            AnimatedVisibility(
                visible = wordProblemList.size > 1
            ) {
                AppLinearProgressIndicator(
                    progress = progress
                )

            }
        }


        NumeracyWordProblem(
            wordProblem = wordProblemList[currentIndex],
            shouldCaptureAnswer = shouldCapture,
            onAnswerImageFilePathChange = { path ->
                onAnswerFilePathChange(path)
            },
            onWorkAreaImageFilePathChange = {path ->
                onWorkOutFilePathChange(path)
            },
            onSubmit = {
                onIsSubmittingChange(true)
            },
            modifier = modifier
        )
    }



}