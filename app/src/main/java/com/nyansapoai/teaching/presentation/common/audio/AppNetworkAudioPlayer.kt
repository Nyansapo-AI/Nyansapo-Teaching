package com.nyansapoai.teaching.presentation.common.audio

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


@Composable
fun AppNetworkAudioPlayer(
    modifier: Modifier = Modifier,
    audioUrl: String?,
    isPaused: Boolean = false,
    hasFinishedPlaying: (Boolean) -> Unit = { }
) {
    val context = LocalContext.current

    // Create and remember ExoPlayer instance
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var isAudioPrepared by remember { mutableStateOf(false) }

    // React to audioUrl changes
    LaunchedEffect(audioUrl) {
        audioUrl?.let { url ->
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            exoPlayer.prepare()
            isAudioPrepared = true
        }
    }

    // React to isPaused changes
    LaunchedEffect(isPaused, isAudioPrepared) {
        if (isAudioPrepared) {
            if (isPaused) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
        }
    }

    // Handle playback state changes with proper listener lifecycle
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    hasFinishedPlaying(true)
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    // Handle cleanup when composable is disposed
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
}