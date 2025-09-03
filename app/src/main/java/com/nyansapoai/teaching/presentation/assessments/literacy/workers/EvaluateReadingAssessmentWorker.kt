package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.presentation.assessments.literacy.components.compareResponseStrings
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class EvaluateReadingAssessmentWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val mediaRepository: MediaRepository by inject()
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository by inject()
    private val localDataSource: LocalDataSource by inject()


    override suspend fun doWork(): Result {
        return try {
            val retryAttempt = runAttemptCount
            Log.d("Worker", "Running")

            val audioFilePath = inputData.getString("audioFilePath") ?: return Result.failure()
            val round = inputData.getInt("round", defaultValue = 0)
            val content = inputData.getString("content") ?: return Result.failure()
            val type = inputData.getString("type") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()
            val studentId = inputData.getString("student_id") ?: return Result.failure()

            val audioBytes = readAudioFile(audioFilePath) ?: return Result.failure()

            val audioUrl = mediaRepository.saveAudio(
                audioByteArray = audioBytes,
                fileName = "audio_${assessmentId}_${studentId}_${round}_${type}_${content.replace(" ", "%20")}.wav"
            ).data ?: return Result.retry()

            Log.d("test audio file","audio url: $audioUrl")

            /*
            val transcription = artificialIntelligenceRepository.getTextFromAudio(audioByteArray = audioBytes).first().data?.DisplayText ?: ""

            val comparison = compareResponseStrings(
                expected = content,
                actual = transcription,
                similarity = 0.9
            )

             */

            localDataSource.insertPendingReadingResult(
                assessmentId = assessmentId,
                studentId = studentId,
                type = type,
                audioUrl = audioUrl,
                content = content,
                transcript = "",
                passed = false,
                timestamp = Clock.System.now().epochSeconds.toInt(),
                isPending = true,
            )

            MediaUtils.cleanUpMediaFile(path = audioFilePath)

            Log.d("Worker", "Running Successful")
            Result.success()
        }catch (e: Exception ){

            Log.d("Worker", "Failed, ${e.message}")
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun handleRetry(attempt: Int): Result {
        return if (attempt >= MAX_RETRIES) {
            Log.e(WORK_NAME, "Max retries reached")
            Result.failure()
        } else {
            Log.d(WORK_NAME, "Scheduling retry ${attempt + 1}")
            Result.retry()
        }
    }


    private fun readAudioFile(path: String): ByteArray? {
        return try {
            File(path).readBytes().also {
                Log.d("EvaluateReadingAssessmentWorker", "Read ${it.size} bytes from audio file")
            }
        } catch (e: Exception) {
            Log.e("EvaluateReadingAssessmentWorker", "Failed to read audio file: ${e.message}", e)
            null
        }
    }

    companion object {
        const val WORK_NAME = "EvaluateReadingAssessmentWorker"
        const val MAX_RETRIES = 4
    }
}