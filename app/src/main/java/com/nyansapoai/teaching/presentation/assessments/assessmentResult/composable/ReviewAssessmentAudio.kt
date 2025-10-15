package com.nyansapoai.teaching.presentation.assessments.assessmentResult.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.audio.AppNetworkAudioPlayer

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewAssessmentAudio(
    modifier: Modifier = Modifier,
//    isPlaying: Boolean = false,
    audioUrl: String? = "",
    studentAnswer: String = "This is a student answer",
    expectedAnswer: String = "This is the expected answer",
    transcript: String = "This is a transcript",
    passed: Boolean = true,
    onPlayPauseClick: () -> Unit = {},
) {

    var isPaused by remember { mutableStateOf(true) }
    var isCompleted by remember { mutableStateOf(false) }


    AnimatedVisibility(
        visible = !isPaused
    ) {
        AppNetworkAudioPlayer(
            audioUrl = audioUrl,
            hasFinishedPlaying = {
                isCompleted = true
                isPaused = true
            }
        )
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Audio Response",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.tertiary)
        )
        {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                IconButton(
                    onClick = {
                        isPaused = !isPaused
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(64.dp)

                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isPaused) R.drawable.play else R.drawable.pause
                        ),
                        contentDescription = "Play/Pause",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(8.dp)
                    )
                }

                Text(
                    text = if (!isPaused) "Pause" else "Play",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Text(
            text = "Expected Answer : $expectedAnswer",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )


        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Text(
                text = "Student Answer",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            TextField(
                value = studentAnswer,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                onValueChange = {},
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent ,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
            )
        }

        Text(
            text = if (passed) "Passed" else "Failed",
            style = MaterialTheme.typography.titleLarge,
            color = if (passed) Color.Green else Color.Red,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Spacer(Modifier.height(20.dp))


    }
}