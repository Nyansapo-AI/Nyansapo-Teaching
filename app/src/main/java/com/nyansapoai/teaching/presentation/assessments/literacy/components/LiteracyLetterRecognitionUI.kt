package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AudioRecorder
import org.koin.compose.koinInject
import java.io.File


private var audioFile: File? = null

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LiteracyLetterRecognitionUI(
    modifier: Modifier = Modifier,

) {



    val context = LocalContext.current

    val audioRecorder = koinInject<AudioRecorder>()
    val audioPlayer = koinInject<AudioPlayer>()

    var showAppIntro by remember {
        mutableStateOf(true)
    }

    var showLetter by remember {
        mutableStateOf(false)
    }




    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = modifier
            .fillMaxSize()
    ){

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Letter Recognition",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    onClick = {
                        showAppIntro = true

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

                TextButton(
                    onClick = {}
                ) {
                    Text(
                        text = "End",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                    )
                }
            }

        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                    text = if (showLetter) "A" else "",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 160.sp,
                )
            }

            IntroShowcase(
                showIntroShowCase = showAppIntro,
                dismissOnClickOutside = false,
                onShowCaseCompleted = {
                    //App Intro finished!!
                    showAppIntro = false
                }
            ) {

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(120.dp)
                        .introShowCaseTarget(
                            index = 0,
                            style = ShowcaseStyle.Default.copy(
//                            backgroundColor = Color(0xFF1C0A00),
                                backgroundColor = MaterialTheme.colorScheme.tertiary,
                                backgroundAlpha = 0.9f,
                                targetCircleColor = MaterialTheme.colorScheme.secondary
                            ),
                            content = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = "Read Letter",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Hold here to read the letter aloud",
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.mic),
                        contentDescription = "Hold to speak",
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.CenterHorizontally)
                            .pointerInput(Unit){
                                detectTapGestures(
                                    onPress = {
                                        showLetter = true

                                        File(context.cacheDir, "audio_recording.m4a").also { file ->
                                            audioRecorder.start(outputFile = file)
                                            audioFile = file
                                        }

                                        tryAwaitRelease()
                                        showLetter = false
                                        audioRecorder.stop()
                                    }
                                )
                            }
                    )
                }

            }
        }

    }

}