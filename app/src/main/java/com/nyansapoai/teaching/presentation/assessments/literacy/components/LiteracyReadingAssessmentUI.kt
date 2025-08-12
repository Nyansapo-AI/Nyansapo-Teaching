package com.nyansapoai.teaching.presentation.assessments.literacy.components

import android.os.Build
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


private var audioFile: File? = null

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LiteracyReadingAssessmentUI(
    modifier: Modifier = Modifier,
    readingList: List<String>,
    currentIndex: Int,
    showInstructions: Boolean,
    onShowInstructionsChange: (Boolean) -> Unit,
    title: String,
    fontSize: TextUnit = 60.sp,
    showContent: Boolean,
    onShowContentChange: (Boolean) -> Unit,
    isLoading: Boolean,
    instructionAudio: Int = R.raw.reading_assessment_default_instructions,
    instructionTitle: String = "Read the letter",
    instructionDescription: String = "Hold the box to record your voice saying the letter.",
    showQuestionNumber: Boolean = true,
    onAudioByteArrayChange: (ByteArray) -> Unit,
    onAudioPathChange: (String) -> Unit,
    audioFilePath: String?,
    response: String?,
    onSubmit: () -> Unit,
) {

    var isButtonClicked by remember { mutableStateOf(false) }

    var allPermissionsAllowed by remember { mutableStateOf(false) }


    RequestAppPermissions(
        permissionsArray =  when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13+ (API 33+): Need RECORD_AUDIO and READ_MEDIA_AUDIO
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.READ_MEDIA_AUDIO
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                // Android 10-12 (API 29-32): Only need RECORD_AUDIO
                arrayOf(android.Manifest.permission.RECORD_AUDIO)
            }

            else -> {
                // Below Android 10 (API < 29): Need legacy storage permissions
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

        },
        onSuccess = {
            allPermissionsAllowed = true
        },
        onFailure = {
            navController.popBackStack()
        },
        content = { action ->
            BasicAlertDialog(
                onDismissRequest = {
                    navController.popBackStack()
                    isButtonClicked = false
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
                            Text(
                                text = "Read and Write Storage",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        AppButton(
                            onClick = {
                                action.invoke()
                                isButtonClicked = true
                            }
                        ) {
                            Text(
                                text = "Allow Permissions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                })
        }
    )

    if (readingList.isEmpty()){
        Text(
            text= "Questions are not available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        return
    }


    if (currentIndex >= readingList.size) {
        Text(
            text = "No more questions available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    val context = LocalContext.current

    val appAudioRecorder = koinInject<AppAudioRecorder>()
    val audioPlayer = koinInject<AudioPlayer>()



    var progress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(currentIndex) {
        progress = if (currentIndex < readingList.size) {
            (currentIndex + 1).toFloat() / readingList.size.toFloat()
        } else {
            1f
        }
    }

    LaunchedEffect(showContent) {
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = modifier
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


            if (showQuestionNumber){
                Text(
                    text = "Question ${currentIndex + 1}/${readingList.size}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            AppLinearProgressIndicator(
                progress = progress
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
                    showInstructions = showInstructions && allPermissionsAllowed,
                    size = 420.dp,
                    instructionsTitle = instructionTitle,
                    instructionsDescription = instructionDescription,
                    instructionAudio = instructionAudio,
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
                                text = if (showContent) {
                                    if (currentIndex < readingList.size) {
                                        readingList[currentIndex]
                                    } else {
                                        "No more questions available"
                                    }
                                } else "",
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



        AppButton(
            enabled = audioFilePath != null,
            onClick = onSubmit,
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