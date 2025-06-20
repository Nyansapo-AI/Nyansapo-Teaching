package com.nyansapoai.teaching.presentation.common.audio.record

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
    fun getOutputFileByteArray(outputFile: File): ByteArray
}