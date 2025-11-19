package com.nyansapoai.teaching.presentation.common.audio.play

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context:Context
): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File, onCompletion: (Boolean) -> Unit) {
        stop()

        val media = MediaPlayer.create(context, file.toUri())

        if (media == null){
            onCompletion(true)
            return
        }


        player = media

        media.setOnCompletionListener {
            onCompletion(true)
            stop()
        }

        media.start()

    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}