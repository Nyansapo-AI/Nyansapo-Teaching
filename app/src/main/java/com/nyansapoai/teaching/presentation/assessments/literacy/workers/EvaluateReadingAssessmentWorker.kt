package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.utils.ResultStatus
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
            Log.d("Worker", "Running")

            val audioFilePath = inputData.getString("audioFilePath") ?: return Result.failure()
            val round = inputData.getInt("round", defaultValue = 0)
            val content = inputData.getString("content") ?: return Result.failure()
            val type = inputData.getString("type") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()
            val studentId = inputData.getString("student_id") ?: return Result.failure()

            val audioBytes = readAudioFile(audioFilePath) ?: return Result.failure()

            val audioUrlResponse = mediaRepository.saveAudio(
                audioByteArray = audioBytes,
                fileName = "audio_${assessmentId}_${studentId}_${round}_${type}_${content.replace(" ", "%20")}.wav"
            )

            Log.d("Worker", "Audio uploaded: $audioUrlResponse")

            when(audioUrlResponse.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING ,
                ResultStatus.ERROR -> {
                    Log.d("Worker", "Audio upload failed or in invalid state: ${audioUrlResponse.message}")
                    return Result.retry()
                }
                ResultStatus.SUCCESS -> {
                    MediaUtils.cleanUpMediaFile(path = audioFilePath)
                }
            }


            Log.d("Worker", "Running Successful")
            Result.success()
        }catch (e: Exception ){

            Log.d("Worker", "Failed, ${e.message}")
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun readAudioFile(path: String): ByteArray? {

        if (path.isEmpty()) {
            return null
        }

        val file = File(path)

        if (!file.exists()){
            return null
        }

        return try {
            file.readBytes().also {
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