package com.nyansapoai.teaching.presentation.common.audio.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class AndroidAudioRecorder(
    private val context: Context
): AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            println("Audio recording started: ${outputFile.absolutePath}")
            recorder = this
        }
    }

    override fun stop() {
        println("Audio recording stopped")
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }

    override fun getOutputFileByteArray(outputFile: File): ByteArray {
        return outputFile.readBytes()
    }
}