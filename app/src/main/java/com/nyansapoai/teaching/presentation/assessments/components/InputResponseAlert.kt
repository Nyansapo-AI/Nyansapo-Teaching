package com.nyansapoai.teaching.presentation.assessments.components

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.bundle.Bundle
import com.nyansapoai.teaching.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputResponseAlert(
    modifier: Modifier = Modifier,
    showAlert: Boolean ,
    response: Int?,
    responseError: String?,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {


    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var canStartSpeech by remember { mutableStateOf(false) }


    val textToSpeak = response?.let { "Is your answer $response ? Press confirm to submit your answer or dismiss to cancel." } ?: "We can't read your answer, Please try again."

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context){ status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)

                // Customize voice properties
                tts?.setSpeechRate(0.85f)  // Slightly slower than default (1.0)
                tts?.setPitch(1.1f)        // Slightly higher pitch than default (1.0)

                // Try to get a female voice if available
                val voices = tts?.voices
                voices?.find { it.name.contains("female", ignoreCase = true) }?.let { femaleVoice ->
                    tts?.setVoice(femaleVoice)
                }


                canStartSpeech = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
    }

    LaunchedEffect(showAlert) {
        if (canStartSpeech && showAlert) {

            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
                putString(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true")
            }


            tts?.speak(
                textToSpeak,
                TextToSpeech.QUEUE_FLUSH,
                params,
                "response_speech"
            )
        }
    }


    AnimatedVisibility(
        visible = showAlert
    ){
        BasicAlertDialog(
            onDismissRequest = {},
        ) {
            AlertDialog(
                onDismissRequest = {},
                  title = {

                    if (responseError != null){
                        Text(
                            text = "We can't read your answer, Please try again.",
//                            text = responseError,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    response?.let {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Your Answer",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "$response",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 120.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }





                },
                text = {
                    Text(
                        text = response?.let { "Press confirm to submit your answer or dismiss to cancel."  } ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                confirmButton = {
                    /*
                    TextButton(
                        onClick = {
                            if (responseError != null) {
                                onDismiss()
                            } else {
                                onConfirm()
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(text = responseError?.let { "Try Again" } ?: "Confirm")
                    }*/

                    IconButton(
                        onClick = {
                            if (responseError != null) {
                                onDismiss()
                            } else {
                                onConfirm()
                            }
                        }
                    ) {
                        responseError?.let {

                        }?:
                            Image(
                                painter = painterResource(R.drawable.like),
                                contentDescription = "Confirm",
                            )
                    }
                },
                dismissButton = {
                    /*
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        )

                    ) {
                        Text(
                            text = "Dismiss"
                        )
                    } */

                    IconButton(
                        onClick = onDismiss
                    ) {
                        Image(
                            painter = painterResource(R.drawable.multiply),
                            contentDescription = "Dismiss",
                        )
                    }
                },
                modifier = modifier
            )
        }

    }
}



@Preview
@Composable
private fun InputResponseAlertPreview() {
//    InputResponseAlert()
}