package com.nyansapoai.teaching.presentation.assessments.literacy.components

import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions
import com.nyansapoai.teaching.ui.theme.lightPrimary
import org.koin.compose.koinInject
import java.io.File
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private var audioFile: File? = null
@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ReadingStoryEvaluationUI(
    modifier: Modifier = Modifier,
    currentIndex: Int = 0,
    title: String = "Reading Story",
    storyTitle: String? = "The Hyena",
    showInstructions: Boolean = true,
    instructionAudio: Int = R.raw.read_letter,
    isLoading: Boolean = false,
    onShowInstructionsChange: (Boolean) -> Unit = {},
    audioFilePath: String? = null,
    onAudioPathChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    storySentencesList: List<String> =
            "Anna and her younger brother Tom were on the way to the market to buy some toys. On the way, they saw their friend Juma. Juma was looking for something. Anna asked Juma, ‘’ What are you looking for?’’. Juma said, ‘’I have lost my money. I have to buy tomatoes for dinner. What will we eat now?\\\" On hearing Juma’s problems, Anna and Tom decided to help him. They bought some tomatoes using their own money. Juma thanked them and rushed home. As he arrived home, he found his lost money by his front door. He picked up the money and ran after Anna and Tom.".split(".")
) {


    val context = LocalContext.current
    val appAudioRecorder = koinInject<AppAudioRecorder>()
    val audioPlayer = koinInject<AudioPlayer>()



    var audioFilePathList by remember { mutableStateOf<List<String>>(emptyList()) }


    var isRecording by remember { mutableStateOf(false) }

    /*
    LaunchedEffect(currentIndex, showInstructions) {
        if (!showInstructions && !isRecording) {
            val file = File(
                context.filesDir,
                "audio_recording_${Clock.System.now().epochSeconds}.wav"
            )
            try {
                appAudioRecorder.start(outputFile = file)
                audioFile = file
                isRecording = true
            } catch (e: Exception) {
                Log.e("AudioFile", "Failed to start recording: ${e.message}")
            }
        } else if (showInstructions && isRecording) {
            try {
                appAudioRecorder.stop()
                isRecording = false
                audioFile?.let {
                    onAudioPathChange(it.absolutePath)
                    audioFilePathList = audioFilePathList + it.absolutePath
//                    audioFile = null
                }
            } catch (e: Exception) {
                Log.e("AudioFile", "Failed to stop recording: ${e.message}")
            }
        }
    }*/

    LaunchedEffect(isRecording) {
        if (!showInstructions && isRecording) {
            val file = File(
                context.filesDir,
                "audio_recording_${Clock.System.now().epochSeconds}.wav"
            )
            try {
                appAudioRecorder.start(outputFile = file)
                audioFile = file
                isRecording = true
            } catch (e: Exception) {
                Log.e("AudioFile", "Failed to start recording: ${e.message}")
            }

        } else{
            try {
                appAudioRecorder.stop()
                isRecording = false
                audioFile?.let {
                    onAudioPathChange(it.absolutePath)
                    audioFilePathList = audioFilePathList + it.absolutePath
                }
            } catch (e: Exception) {
                Log.e("AudioFile", "Failed to stop recording: ${e.message}")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                appAudioRecorder.stop()
            } catch (e: Exception) {
                Log.e("AudioFile", "Failed to stop recording on dispose: ${e.message}")
            }
            audioFile = null
            isRecording = false
        }
    }



    RequestAppPermissions(
        permissionsArray =  when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13+ (API 33+): Need RECORD_AUDIO and READ_MEDIA_AUDIO
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.READ_MEDIA_AUDIO
                )
            }
            else -> {
                // Below Android 10 (API < 29): Need legacy storage permissions
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                )
            }

        },
        onSuccess = {
        },
        onFailure = {
            navController.popBackStack()
        },
        content = { action ->
            BasicAlertDialog(
                onDismissRequest = {
                    navController.popBackStack()
                },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Allow The Following Permissions Requests to Do the Assessment.",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "Record Audio",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        AppButton(
                            onClick = {
                                action.invoke()
                            }
                        ) {
                            Text(
                                text = "Allow Permissions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                }
            )
        }
    )

    if (storySentencesList.isEmpty()){
        Text(
            text= "The questions are not available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    if (currentIndex >= storySentencesList.size) {
        Text(
            text = "No more questions available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    val listState = rememberLazyListState()

    /*
    LaunchedEffect(currentIndex) {
        listState.animateScrollToItem(currentIndex)
    } */


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {onShowInstructionsChange(!showInstructions)}
            ) {
                Icon(
                    painter = painterResource(R.drawable.volume),
                    contentDescription = "click for instructions",
                )
            }

        }




        /*
        TextButton(
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            onClick = {
                onShowInstructionsChange(!showInstructions)
            }
        )
        {
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
        }*/

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .fillMaxSize()
        )
        {

            if (!storyTitle.isNullOrEmpty()){
                Text(
                    text = storyTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                        .background(androidx.compose.ui.graphics.Color.Transparent)

                ) {
                    AppShowInstructions(
                        showInstructions = showInstructions,
                        instructionsTitle = "Reading Story",
                        instructionsDescription = "Tap the microphone. Read the story out loud. Tap the microphone again when you finish.",
                        instructionAudio = R.raw.reading_story_instruction,
                        onChangeShow = {show -> onShowInstructionsChange(show)},
                        hasCompletedPlaying = { hasCompleted -> onShowInstructionsChange(!hasCompleted) }
                    ) {

                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = if(isRecording) MaterialTheme.colorScheme.error else lightPrimary
                            ),
                            onClick = {
                                isRecording = !isRecording
                            },
                            enabled = !showInstructions,
                            modifier = Modifier
                                .size(80.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.mic),
                                contentDescription = "start recording",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }

                    }


                    AnimatedVisibility(
                        visible = !isRecording && audioFilePath != null,
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    ) {
                        AppShowInstructions(
                            showInstructions = false,
                            instructionsTitle = "Next Sentence",
                            onChangeShow = { show -> onShowInstructionsChange(show) },
                            index = 1,
                            instructionAudio = R.raw.click_the_button_to_move_to_the_next_sentence,
                            instructionsDescription = "click to move to the next sentence",
                            modifier = Modifier
                        )
                        {

                            AppButton(
                                enabled = !isLoading && !showInstructions && !isRecording && audioFilePath != null ,
                                onClick = {
                                    appAudioRecorder.stop()
                                    isRecording = false
                                    audioFile?.let {
                                        onAudioPathChange(it.absolutePath)
                                        audioFilePathList = audioFilePathList + it.absolutePath
                                        audioFile = null
                                        onSubmit.invoke()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Next",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }
                }


                AppShowInstructions(
                    showInstructions = false,
                    instructionsTitle = "Read the sentence",
                    index = 0,
                    onChangeShow = { show -> onShowInstructionsChange(show) },
                    instructionAudio = R.raw.read_the_sentence_in_yellow,
                    instructionsDescription = "Read the sentence in yellow",
                    content = {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 72.dp)
                        )
                        {
                            itemsIndexed(
                                items = storySentencesList
                            )
                            { index, it ->
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Center,
                                    style = if (index == currentIndex) {
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
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

        }
    }
}

