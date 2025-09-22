package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UploadNumeracyReadingAssessmentAudioWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {
    private val mediaRepository: MediaRepository by inject()

    override suspend fun doWork(): Result {
        return try {

            val audioFilePath = inputData.getString(AUDIO_FILE_PATH) ?: return Result.failure()
            val round = inputData.getInt(ROUND, defaultValue = 0)
            val content = inputData.getString(CONTENT) ?: return Result.failure()
            val type = inputData.getString(TYPE) ?: return Result.failure()
            val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
            val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()

            val audioBytes = MediaUtils.readImageFileByteArray(path = audioFilePath) ?: return Result.failure()

            val saveAudioResponse = mediaRepository.saveAudio(
                audioByteArray = audioBytes,
                fileName = "audio_${assessmentId}_${studentId}_${round}_${type}_${content.replace(" ", "%20")}.wav"
            )


            when(saveAudioResponse.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    return Result.retry()
                }
                ResultStatus.SUCCESS -> {
                    MediaUtils.cleanUpMediaFile(path = audioFilePath)
                }
                ResultStatus.ERROR -> {
                    return Result.failure()
                }
            }

            Result.success()
        }catch (e: Exception ){
            e.printStackTrace()
            Result.failure()
        }
    }


    companion object {
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"
        const val ROUND = "round"
        const val AUDIO_FILE_PATH = "audioFilePath"
        const val CONTENT = "content"
        const val TYPE = "type"

    }
}