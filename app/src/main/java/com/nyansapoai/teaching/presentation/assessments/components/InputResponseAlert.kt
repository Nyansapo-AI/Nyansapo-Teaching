package com.nyansapoai.teaching.presentation.assessments.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.audio.AppLocalAudioPlayer

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


    /*
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var canStartSpeech by remember { mutableStateOf(false) }


    val textToSpeak = response?.let { "Is your answer $response ?" } ?: "We can't read your answer, Please try again."

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context){ status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)

                // Customize voice properties
                tts?.setSpeechRate(0.95f)  // Slightly slower than default (1.0)
                tts?.setPitch(1.0f)        // Slightly higher pitch than default (1.0)

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
    } */

    responseError?.let {
        AppLocalAudioPlayer(
            audio = R.raw.child_error
        )
    }


    AnimatedVisibility(
        visible = showAlert,
        modifier = modifier,
    ){
        BasicAlertDialog(
            onDismissRequest = onDismiss,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(12.dp)
            ) {

                response?.let {


                    Text(
                        text = "Your Answer",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        text = response.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 120.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp)
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier
//                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(60.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.multiply),
                                contentDescription = "Dismiss",
                                modifier = Modifier
                                    .size(48.dp)
                            )
                        }


                        IconButton(
                            onClick = onConfirm,
                            modifier = Modifier
                                .size(60.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.like),
                                contentDescription = "Confirm",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(48.dp)
                            )

                        }
                    }
                }

                responseError?.let {
                    Text(
                        text = "We can't read your answer, Please try again.",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp)
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(60.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.multiply),
                            contentDescription = "Dismiss",
                            modifier = Modifier
                                .size(48.dp)
                        )
                    }



                }
            }


            /*
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
                        },
                        modifier = Modifier
                            .size(60.dp)
                    ) {
                        responseError?.let {

                        }?:
                            Image(
                                painter = painterResource(R.drawable.like),
                                contentDescription = "Confirm",
                                contentScale = ContentScale.Fit
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
            )*/
        }

    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InputResponseAlertPreview() {
    InputResponseAlert(
        showAlert = true,
        response = 42,
        responseError = null,
        onConfirm = {},
        onDismiss = {}
    )
}