package com.nyansapoai.teaching.presentation.common.audio.record

import java.io.File

interface AppAudioRecorder {
    fun start(outputFile: File)
    fun stop()
    fun getOutputFileByteArray(outputFile: File): ByteArray
}