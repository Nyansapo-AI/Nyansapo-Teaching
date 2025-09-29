package com.nyansapoai.teaching.presentation.common.audio

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions

@Composable
fun AppAudio(
    modifier: Modifier = Modifier,
    audio: Int? = null
) {
    var isPermissionGranted by remember { mutableStateOf(false) }

    /*
    RequestAppPermissions(
        permissionsArray = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ),
        onSuccess = {
            isPermissionGranted = true
        },
    )*/


    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        audio?.let {
            mediaPlayer = MediaPlayer.create(context, audio)
            mediaPlayer?.start()
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    /*
    Column {
        Button(
            onClick = {
                mediaPlayer?.let { player ->
                    if (isPlaying) {
                        player.pause()
                        isPlaying = false
                    } else {
                        player.start()
                        isPlaying = true
                    }
                }
            }
        ) {
            Text(if (isPlaying) "Pause" else "Play")
        }
    } */
}