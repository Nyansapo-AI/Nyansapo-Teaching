package com.nyansapoai.teaching.presentation.assessments.literacy.components

import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.animations.AppLottieAnimations
import com.nyansapoai.teaching.presentation.common.animations.RiveAnimation
import com.nyansapoai.teaching.presentation.common.audio.AppLocalAudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppCountDown
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions
import com.nyansapoai.teaching.ui.theme.abeeZeeFont
import com.nyansapoai.teaching.ui.theme.lightPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.io.File
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


private var audioFile: File? = null

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class,
    ExperimentalMaterial3ExpressiveApi::class
)
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
    instructionAudio: Int = R.raw.read_letter,
    instructionTitle: String = "Read the letter",
    instructionDescription: String = "Hold the box to record your voice saying the letter.",
    showQuestionNumber: Boolean = true,
    onAudioPathChange: (String) -> Unit,
    audioFilePath: String?,
    onSubmit: () -> Unit,
) {

    var allPermissionsAllowed by remember { mutableStateOf(false) }
    var listenBack by remember { mutableStateOf(false) }


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
//            delay(50)
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


    /**
     * Listen back to the audio file
     *
     */
    LaunchedEffect(listenBack) {
        when (listenBack) {
            true -> {
                if (!audioFilePath.isNullOrBlank()) {
                    // If audioFilePath is an absolute path use it directly,
                    // otherwise assume it's a filename inside context.filesDir
                    val fileToPlay = if (File(audioFilePath).isAbsolute) {
                        File(audioFilePath)
                    } else {
                        File(context.filesDir, audioFilePath)
                    }

                    if (fileToPlay.exists()) {
                        audioPlayer.playFile(fileToPlay)
                    } else {
                        Log.w("Playback", "Audio file does not exist: ${fileToPlay.absolutePath}")
                    }
                } else {
                    Log.w("Playback", "audioFilePath is null or blank, cannot play")
                }
            }
            false -> {
                audioPlayer.stop()
            }
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            appAudioRecorder.stop()
            audioPlayer.stop()
            audioFile = null
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

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        onClick = {onShowInstructionsChange(!showInstructions)}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.volume),
                            contentDescription = "Instructions Icon",
//                            tint = if (showInstructions) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    /*
                    Text(
                        text = "Tap to hear instructions",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                    )

                     */
                }
            }


            if (showQuestionNumber){
                Text(
                    text = "Question ${currentIndex + 1}/${readingList.size}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            AppLinearProgressIndicator(
                progress = progress
            )
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
                    onChangeShow = {  show -> onShowInstructionsChange(show) },
                    hasCompletedPlaying = { completed -> if (completed) onShowInstructionsChange(false) },
                    content = {
                        AnimatedVisibility(
                            visible = showInstructions,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                                .zIndex(1.0f)
                                .clickable(
                                    onClick = { onShowInstructionsChange(false) }
                                )
                        ) {
                            RiveAnimation(
                                animation = R.raw.pointer,
                                modifier = Modifier
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .heightIn(min = 200.dp, max = 640.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5))
                                .background(boxColor)
                                .zIndex(0.9f)
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
                                fontFamily = abeeZeeFont,
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

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(
                visible = audioFilePath != null && !showContent,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 250)
                )
            ) {
                TextButton(
                    enabled = audioFilePath != null && !showContent,
                    onClick = {
                        listenBack = !listenBack
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = lightPrimary
                    )
                )
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding( 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.volume),
                            contentDescription = "Listen to your recording"
                        )
                        Text(
                            text = "Listen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
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

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreTestReadingAssessmentUI(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = { }
)
{

    val coroutineScope = rememberCoroutineScope()

    var showInstructions by remember { mutableStateOf(true) }
    var showContent by remember { mutableStateOf(false) }
    var filePath by remember { mutableStateOf<String?>(null) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var showCountDown by remember { mutableStateOf(false) }
    var showAudioInstructions by remember { mutableStateOf(true) }



    val readingList  = listOf("a", "b", "boy", "cat")

    AnimatedContent(
        targetState = showAudioInstructions
    ) { state ->
        
        when(state){
            true -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .widthIn(max = 420.dp)
                ) {
                    AppLottieAnimations(
                        resId = R.raw.talking_boy,
                        modifier = Modifier
                            .size(300.dp)
                            .align(Alignment.CenterStart)
                    )

                    Text(
                        text = "Let's Practice for the assessment.",
                        style = MaterialTheme.typography.titleSmall,
                        color = contentColorFor(lightPrimary),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 180.dp)
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(lightPrimary)
                            .padding(16.dp)

                    )

                }
                AppLocalAudioPlayer(
                    audio = R.raw.lets_practice_for_the_assessment,
                    hasFinishedPlaying = {completed ->
                        coroutineScope.launch {
                            delay(500)
                            if (completed) showAudioInstructions = false
                        }
                    }
                )

            }
            false -> {
                AnimatedContent(
                    targetState = showCountDown
                )
                { targetState ->
                    when(targetState){
                        true -> {
                            AppCountDown(
                                seconds = 3,
                                onFinish = {
                                    onStart.invoke()
                                }
                            )

                            AppLocalAudioPlayer(
                                audio = R.raw.good_job_get_ready_to_start_the_assessment
                            )
                        }
                        false -> {
                            LiteracyReadingAssessmentUI(
                                readingList = readingList ,
                                currentIndex = currentIndex,
                                showInstructions = showInstructions,
                                onShowInstructionsChange = {
                                    showInstructions = it
                                },
                                title = "Practice Reading",
                                fontSize = 100.sp,
                                showContent = showContent,
                                onShowContentChange = {
                                    showContent = it
                                },
                                isLoading = false,
                                onAudioPathChange = {
                                    filePath = it
                                },
                                audioFilePath = filePath,
                                showQuestionNumber = false,
                                instructionAudio = R.raw.read_letter,
                                instructionTitle = "Read the letter",
                                instructionDescription = "Hold the box to record your voice saying the letter.",
                                onSubmit = {
                                    if (currentIndex >= readingList.size - 1){
                                        showCountDown = true
                                        return@LiteracyReadingAssessmentUI
                                    }
                                    currentIndex ++
                                    MediaUtils.cleanUpMediaFile(path = filePath ?: return@LiteracyReadingAssessmentUI)
                                    filePath = null

                                },
                                modifier = modifier
                            )

                        }
                    }
                }

            }
        }

    }




}