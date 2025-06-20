package com.nyansapoai.teaching.presentation.common.audio.play

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}