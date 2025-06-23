package com.nyansapoai.teaching.presentation.common.audio.record

import android.Manifest
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import java.io.File
import kotlin.concurrent.thread

class AndroidAppAudioRecorder(
    private val context: Context
): AppAudioRecorder {

    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var recordingThread: Thread? = null

    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bitsPerSample = 16 // For ENCODING_PCM_16BIT

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override fun start(outputFile: File) {
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        ).apply { startRecording() }

        isRecording = true

        // Create temporary PCM file
        val pcmFile = File("${outputFile.absolutePath}.pcm")

        recordingThread = thread {
            try {
                val buffer = ByteArray(bufferSize)
                pcmFile.outputStream().use { output ->
                    while (isRecording) {
                        val read = audioRecord?.read(buffer, 0, bufferSize) ?: -1
                        if (read > 0) {
                            output.write(buffer, 0, read)
                        }
                    }
                }

                // Create WAV header
                val channels = if (channelConfig == AudioFormat.CHANNEL_IN_MONO) 1 else 2
                val header = createWavHeader(pcmFile.length(), sampleRate, channels, bitsPerSample)

                // Write header and PCM data to final WAV file
                outputFile.outputStream().use { output ->
                    output.write(header)
                    pcmFile.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }

                // Delete temporary PCM file
                pcmFile.delete()

                println("Audio recording saved as WAV: ${outputFile.absolutePath}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun stop() {
        println("Audio recording stopped")
        isRecording = false

        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null

        // Wait for recording thread to finish
        recordingThread?.join()
        recordingThread = null
    }

    override fun getOutputFileByteArray(outputFile: File): ByteArray {
        return outputFile.readBytes()
    }

    private fun createWavHeader(
        pcmDataLength: Long,
        sampleRate: Int,
        channels: Int,
        bitsPerSample: Int
    ): ByteArray {
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val fileSize = 36 + pcmDataLength

        return ByteArray(44).apply {
            // RIFF header
            System.arraycopy("RIFF".toByteArray(), 0, this, 0, 4)
            // ChunkSize (file size - 8)
            putLittleEndianInt(this, 4, (fileSize - 8).toInt())
            System.arraycopy("WAVE".toByteArray(), 0, this, 8, 4)

            // fmt subchunk
            System.arraycopy("fmt ".toByteArray(), 0, this, 12, 4)
            putLittleEndianInt(this, 16, 16) // Subchunk1Size (16 for PCM)
            putLittleEndianShort(this, 20, 1) // AudioFormat (1 for PCM)
            putLittleEndianShort(this, 22, channels.toShort()) // NumChannels
            putLittleEndianInt(this, 24, sampleRate) // SampleRate
            putLittleEndianInt(this, 28, byteRate) // ByteRate
            putLittleEndianShort(this, 32, (channels * bitsPerSample / 8).toShort()) // BlockAlign
            putLittleEndianShort(this, 34, bitsPerSample.toShort()) // BitsPerSample

            // data subchunk
            System.arraycopy("data".toByteArray(), 0, this, 36, 4)
            putLittleEndianInt(this, 40, pcmDataLength.toInt()) // Subchunk2Size
        }
    }

    private fun putLittleEndianInt(bytes: ByteArray, offset: Int, value: Int) {
        bytes[offset] = (value and 0xFF).toByte()
        bytes[offset + 1] = ((value shr 8) and 0xFF).toByte()
        bytes[offset + 2] = ((value shr 16) and 0xFF).toByte()
        bytes[offset + 3] = ((value shr 24) and 0xFF).toByte()
    }

    private fun putLittleEndianShort(bytes: ByteArray, offset: Int, value: Short) {
        bytes[offset] = (value.toInt() and 0xFF).toByte()
        bytes[offset + 1] = ((value.toInt() shr 8) and 0xFF).toByte()
    }
}