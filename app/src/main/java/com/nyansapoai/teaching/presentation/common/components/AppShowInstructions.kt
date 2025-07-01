package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.audio.AppAudio

@Composable
fun AppShowInstructions(
    modifier: Modifier = Modifier,
    showInstructions: Boolean = true,
    onChangeShow: (Boolean) -> Unit = {  },
    index: Int = 0,
    instructionAudio: Int = R.raw.reading_assessment_default_instructions,
    size: Dp = Dp.Unspecified,
    instructionsTitle: String = "Instructions",
    instructionsDescription: String = "Follow the instructions carefully to complete the task.",
    content: @Composable () -> Unit = {  }
) {


    AnimatedVisibility(
        visible = showInstructions
    ) {
        AppAudio(
            audio = instructionAudio
        )
    }

    IntroShowcase(
        showIntroShowCase = showInstructions,
        dismissOnClickOutside = false,
        onShowCaseCompleted = {
            onChangeShow(false)
        },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(size)
                .introShowCaseTarget(
                    index = index,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color(0xFF1C0A00),
                        backgroundAlpha = 0.98f,
                        targetCircleColor = MaterialTheme.colorScheme.secondary
                    ),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = instructionsTitle,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = instructionsDescription,
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 16.sp
                            )
                        }

                    }
                )
        ) {
            content.invoke()
        }

    }
}