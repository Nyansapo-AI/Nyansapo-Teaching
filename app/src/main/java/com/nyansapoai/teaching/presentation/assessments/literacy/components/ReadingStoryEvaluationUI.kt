package com.nyansapoai.teaching.presentation.assessments.literacy.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Clock
import java.io.File
import kotlin.time.ExperimentalTime
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder

private var audioFile: File? = null
@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ReadingStoryEvaluationUI(
    modifier: Modifier = Modifier,
    currentIndex: Int = 3,
    showInstructions: Boolean = true,
    showContent: Boolean = false,
    isLoading: Boolean = false,
    response: String? = null,
    onShowInstructionsChange: (Boolean) -> Unit = {},
    onShowContentChange: (Boolean) -> Unit = {},
    onAudioPathChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    storySentencesList: List<String> = listOf(
        "The cat is on the mat.",
        "The dog barks loudly.",
        "Birds are flying in the sky.",
        "The sun is shining bright.",
        "Children are playing in the park."
    )
) {

    val context = LocalContext.current
    val appAudioRecorder = koinInject<AppAudioRecorder>()
    val audioPlayer = koinInject<AudioPlayer>()


    val animatedFontSize by animateFloatAsState(
        targetValue = if (currentIndex == -1) 16f else 24f
    )
    val animatedColor by animateColorAsState(
        targetValue = if (currentIndex == -1) Color.Black else Color.Blue
    )
    val animatedFontWeight = if (currentIndex == -1) null else FontWeight.Bold


    var focused by remember { mutableStateOf(0) }

    LaunchedEffect(focused) {
        if (showContent){
            File(
                context.filesDir,
                "audio_recording_${Clock.System.now().epochSeconds}.wav"
            ).also { file ->
                appAudioRecorder.start(outputFile = file)
                audioFile = file
            }
        }else if (audioFile != null){
            delay(1000)
            appAudioRecorder.stop()
            audioFile?.let {
                when {
                    !isLoading -> {
                        Log.d("AudioFile", "Audio file path: ${it.absolutePath}")
                        onAudioPathChange(it.absolutePath)
                        audioFile = null
                    }
                }
            }?: return@LaunchedEffect
        }
    }




    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Reading Story Evaluation UI",
            fontSize = 20.sp,
            modifier = Modifier
//                .padding(16.dp)
        )


        var scrollOffset by remember { mutableStateOf(0f) }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        scrollOffset += dragAmount.y

                        /*
                        if (scrollOffset > 50) {
                            // User dragged down
                            focused = (focused - 1).coerceIn(0, storySentencesList.size - 1)
                            scrollOffset = 0f
                        } else if (scrollOffset < -50) {
                            // User dragged up
                            focused = (focused + 1).coerceIn(0, storySentencesList.size - 1)
                            scrollOffset = 0f
                        }*/
                        if (scrollOffset < -50){

                            // User dragged up
                            focused = (focused + 1).coerceIn(0, storySentencesList.size - 1)
                            scrollOffset = 0f
                        }
                    }

                }
                /*
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { delta ->
                        /*
                        if (delta > 0) {
                            if (focused > storySentencesList.size){
                                Log.d("ReadingStoryEvaluation", "Reached the end of the list")
                                focused = 0
                                return@rememberScrollableState 0f
                            }
                            focused++
                        }

                         */

                        if (delta > 0) {
                            focused = (focused + 1).coerceIn(0, storySentencesList.size - 1)
                        } else if (delta < 0) {
                            focused = (focused - 1).coerceIn(0, storySentencesList.size - 1)
                        }
                        0f // Consume the scroll
                    }
                )*/
        )
        {

            storySentencesList.forEachIndexed { index, it ->
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = if (index == focused) {
                        TextStyle(
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        TextStyle(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    },
                )
            }

        }

    }
}

