package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UploadNumeracyArithmeticOperationImageWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {
    private val mediaRepository: MediaRepository by inject()

    override suspend fun doWork(): Result {

        val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
        val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
        val answerImageFilePath = inputData.getString(ANSWER_IMAGE_FILE_PATH) ?: return Result.failure()
        val workoutImageFilePath = inputData.getString(WORKOUT_IMAGE_FILE_PATH) ?: return Result.failure()
        val round = inputData.getInt(ROUND, 0)
        val operationType = inputData.getString(OPERATION_TYPE) ?: return Result.failure()
        val operationNumber1 = inputData.getInt(OPERATION_NUMBER1, 0)
        val operationNumber2 = inputData.getInt(OPERATION_NUMBER2, 0)
        val expectedAnswer = inputData.getInt(EXPECTED_ANSWER, 0)

        if (assessmentId.isEmpty() || studentId.isEmpty()) {
            Log.e("SubmitNumeracyArithmeticOperationResultsWorker", "Missing assessmentId or studentId")
            return Result.failure()
        }

        try {
            val answerImageByteArray = MediaUtils.readImageFileByteArray(path = answerImageFilePath) ?: return Result.failure()

            val uploadAnswerImageResponse = mediaRepository.saveImage(
                imageByteArray = answerImageByteArray,
                fileName = "image_answer_${assessmentId}_${studentId}_${round}_${operationNumber1}_${operationType}_${operationNumber2}_${expectedAnswer}.wav"
            )

            when(uploadAnswerImageResponse.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    return Result.retry()
                }
                ResultStatus.ERROR -> {
                    return Result.failure()
                }
                ResultStatus.SUCCESS -> {
                    MediaUtils.cleanUpMediaFile(path = answerImageFilePath)
                }
            }

            val workoutImageByteArray = MediaUtils.readImageFileByteArray(path = workoutImageFilePath) ?: return Result.failure()

            val uploadWorkoutImageResponse = mediaRepository.saveAudio(
                audioByteArray = workoutImageByteArray,
                fileName = "image_workArea_${assessmentId}_${studentId}_${round}_${operationNumber1}_${operationType}_${operationNumber2}_${expectedAnswer}_workout.wav"
            )

            when(uploadWorkoutImageResponse.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    return Result.retry()
                }
                ResultStatus.ERROR -> {
                    return Result.failure()
                }
                ResultStatus.SUCCESS -> {
                    MediaUtils.cleanUpMediaFile(path = workoutImageFilePath)
                }
            }

            return Result.success()

        }catch (e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
    }

    companion object {
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"
        const val ANSWER_IMAGE_FILE_PATH = "answer_image_file_path"
        const val WORKOUT_IMAGE_FILE_PATH = "workout_image_file_path"
        const val OPERATION_NUMBER1 = "operation_number1"
        const val OPERATION_NUMBER2 = "operation_number2"
        const val EXPECTED_ANSWER = "expected_answer"
        const val OPERATION_TYPE = "operation_type"
        const val ROUND = "round"

    }
}