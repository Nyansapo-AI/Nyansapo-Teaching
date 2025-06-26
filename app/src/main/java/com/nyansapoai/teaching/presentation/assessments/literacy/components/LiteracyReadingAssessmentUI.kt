package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File


private var audioFile: File? = null

@Composable
fun LiteracyReadingAssessmentUI(
    modifier: Modifier = Modifier,
    letters: List<String>,
    currentIndex: Int,
    showInstructions: Boolean,
    onShowInstructionsChange: (Boolean) -> Unit,
    title: String,
    fontSize: TextUnit = 60.sp,
    showContent: Boolean,
    onShowContentChange: (Boolean) -> Unit,
    isLoading: Boolean,
    audioByteArray: ByteArray?,
    onAudioByteArrayChange: (ByteArray) -> Unit,
    response: String?,
    onSubmit: () -> Unit,
) {


    val context = LocalContext.current

    val appAudioRecorder = koinInject<AppAudioRecorder>()
    val audioPlayer = koinInject<AudioPlayer>()



    var progress by remember {
        mutableFloatStateOf(0f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )

    LaunchedEffect(currentIndex) {
        if (currentIndex < letters.size) {
            progress = (currentIndex + 1).toFloat() / letters.size.toFloat()
        } else {
            progress = 1f
        }
    }

    LaunchedEffect(showContent) {
        if (showContent){
            File(
                context.cacheDir,
                "audio_recording.wav"
            ).also { file ->
                appAudioRecorder.start(outputFile = file)
                audioFile = file
            }

        }else if (audioFile != null){
            delay(1000)
            appAudioRecorder.stop()
            audioFile?.let {

                when {
                    audioFile != null && !isLoading -> {
                        onAudioByteArrayChange(appAudioRecorder.getOutputFileByteArray(outputFile = audioFile!!))
                        onSubmit.invoke()
                        audioFile = null
                    }
                }
            }
        }
    }

    if (letters.isEmpty()){
        return
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
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )


            Text(
                text = "Question ${currentIndex + 1}/${letters.size}",
                style = MaterialTheme.typography.titleMedium,
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
            )

            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    onShowInstructionsChange(!showInstructions)

                    audioFile?.let {
                        audioPlayer.playFile(it)
                    } ?: run {
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
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
            ) {

                val boxColor by animateColorAsState(
                    if (!showContent) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary,
                    label = "boxColorAnimation"
                )



                AppShowInstructions(
                    showInstructions = showInstructions,
                    size = 420.dp,
                    instructionsTitle = "Read the letter",
                    instructionsDescription = "Hold the box to record your voice saying the letter.",
                    content = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .heightIn(min = 200.dp, max = 640.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5))
                                .background(boxColor)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            onShowContentChange(false)
                                        },
                                        onPress = {
                                            onShowContentChange(true)
                                            tryAwaitRelease()
                                            onShowContentChange(false)
                                        }
                                    )
                                }
                        ) {
                            Text(
                                text = if (showContent) letters[currentIndex] else "",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = fontSize,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.Center)
                            )
                        }

                    }
                )

            }
        }


        /*
        AppButton(
            enabled = audioByteArray != null,
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }*/
    }

}