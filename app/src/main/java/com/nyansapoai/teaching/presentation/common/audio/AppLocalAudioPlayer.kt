package com.nyansapoai.teaching.presentation.common.audio

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppLocalAudioPlayer(
    modifier: Modifier = Modifier,
    audio: Int? = null,
    hasFinishedPlaying: (Boolean) -> Unit = { }
) {

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(audio) {
        audio?.let {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, audio)
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
                hasFinishedPlaying(true)
            }
            mediaPlayer?.start()
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

}