package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UploadNumeracyWordProblemImagesWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val mediaRepository: MediaRepository by inject()

    override suspend fun doWork(): Result {
        try {
            /**
             * get the input data from the worker parameters
             */
            val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
            val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
            val question = inputData.getString(QUESTION) ?: return Result.failure()
            val expectedAnswer = inputData.getInt(EXPECTED_ANSWER, 0)
            val answerImagePath = inputData.getString(ANSWER_IMAGE_PATH) ?: return Result.failure()
            val workoutImagePath = inputData.getString(WORKOUT_IMAGE_PATH) ?: return Result.failure()
            val round = inputData.getInt(ROUND, 0)

            val answerImageByteArray = MediaUtils.readImageFileByteArray(path = answerImagePath) ?: return Result.failure()

            val uploadAnswerImageResponse = mediaRepository.saveImage(
                imageByteArray = answerImageByteArray,
                folder = "Word_Problem_Image",
                fileName = "word_problem_${assessmentId}_${studentId}_${question.replace(" ", "%20")}_${expectedAnswer}.wav"
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
                    MediaUtils.cleanUpMediaFile(path = answerImagePath)
                }
            }

            val workoutImageByteArray = MediaUtils.readImageFileByteArray(path = workoutImagePath) ?: return Result.failure()

            val uploadWorkoutImageResponse = mediaRepository.saveAudio(
                audioByteArray = workoutImageByteArray,
                folder = "Nyansapo_Teaching_Numeracy_Assessment_test_workout_Images",
                fileName = "image_workout_${assessmentId}_${studentId}_${round}_wordProblem_${question}_${expectedAnswer}.wav"
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
                    MediaUtils.cleanUpMediaFile(path = workoutImagePath)
                }
            }

            return Result.success()
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }


    companion object {
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"
        const val QUESTION = "question"
        const val ROUND = "round"
        const val EXPECTED_ANSWER = "expected_answer"
        const val ANSWER_IMAGE_PATH = "answer_image_path"
        const val WORKOUT_IMAGE_PATH = "workout_image_path"
        const val WORK_NAME = "EvaluateNumeracyWordProblemWorker"
    }
}