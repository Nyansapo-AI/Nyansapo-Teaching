package com.nyansapoai.teaching.presentation.common.audio

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun AndroidNetworkAudioPlayer(
    modifier: Modifier = Modifier,
    audioUrl: String,
) {
    val context = LocalContext.current

    // Create and remember ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(audioUrl)))
            prepare()
        }
    }

    // Handle cleanup
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                if (isPlaying) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
                isPlaying = !isPlaying
            }
        ) {
            Text(
                if (isPlaying) "Pause" else "Play",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}