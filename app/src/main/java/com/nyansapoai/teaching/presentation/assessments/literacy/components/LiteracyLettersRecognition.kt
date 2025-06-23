package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import org.koin.compose.koinInject
import java.io.File


private var audioFile: File? = null

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LiteracyLettersRecognitionUI(
    modifier: Modifier = Modifier,
    letters: List<String> =  listOf("i", "j", "k", "m", "x", "r", "b", "c", "d"),
    onClick: () -> Unit = {}
) {

    val context = LocalContext.current

    var audioByteArray by remember {
        mutableStateOf<ByteArray?>(null)
    }


    val appAudioRecorder = koinInject<AppAudioRecorder>()
    val audioPlayer = koinInject<AudioPlayer>()

    var currentIndex by remember { mutableStateOf(0) }

    var showInstructions by remember {
        mutableStateOf(true)
    }

    var showLetter by remember {
        mutableStateOf<Boolean?>(null)
    }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = modifier
            .fillMaxSize()
            .widthIn(max = 700.dp)
            .padding(16.dp),
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(
                text = "Letter Recognition",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )


            Text(
                text = "Question ${currentIndex + 1}/${letters.size}",
                style = MaterialTheme.typography.titleMedium,
            )

            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                progress = (currentIndex + 1f / letters.size).toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    showInstructions = !showInstructions

                    audioFile?.let {
                        audioPlayer.playFile(it)
                    }?: run {
                        println("Audio file not available")
                        // Handle case where audio file is not available
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.play),
                        contentDescription = "click for instructions"
                    )

                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
//                    .fillMaxSize()
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .heightIn(min = 200.dp, max = 300.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(
                        text = if (showLetter == true) letters[currentIndex] else "",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 160.sp,
                    )
                }

                AppShowInstructions(
                    showInstructions = showInstructions,
                    size = 120.dp,
                    instructionsTitle = "Read the letter",
                    instructionsDescription = "Hold the button below to record your voice saying the letter.",
                    content = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.mic),
                                contentDescription = "Hold to speak",
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                showLetter = false
                                            },
                                            onPress = {
                                                showLetter = true

                                                File(
                                                    context.cacheDir,
                                                    "audio_recording.wav"
                                                ).also { file ->
                                                    appAudioRecorder.start(outputFile = file)
                                                    audioFile = file


                                                }

                                                tryAwaitRelease()
                                                showLetter = false
                                                appAudioRecorder.stop()

                                                audioFile?.let {
                                                    println("Audio file recorded: ${audioFile!!.absolutePath}")
                                                    println("Audio file recorded byteArray: ${appAudioRecorder.getOutputFileByteArray(outputFile = audioFile!!)}")

                                                    when {
                                                        audioFile != null -> {
                                                            audioByteArray = appAudioRecorder.getOutputFileByteArray(outputFile = audioFile!!)
                                                        }
                                                    }
                                                }

                                            }
                                        )
                                    }
                            )
                        }
                    }
                )

            }
        }


        AppButton(
            enabled = audioByteArray != null,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }

}