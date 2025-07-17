package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.domain.dto.ai.GetTextFromImageRequestDTO
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationMetadata
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.assessments.literacy.components.compareResponseStrings
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EvaluateNumeracyWordProblemWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val localDataSource: LocalDataSource by inject()
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository by inject()
    private val mediaRepository: MediaRepository by inject()

    override suspend fun doWork(): Result {
        val retryAttempt = runAttemptCount
        try {
            /**
             * get the input data from the worker parameters
             */
            val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
            val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
            val question = inputData.getString(QUESTION) ?: return Result.failure()
            val expectedAnswer = inputData.getInt(EXPECTED_ANSWER, 0) ?: return Result.failure()
            val answerImagePath = inputData.getString(ANSWER_IMAGE_PATH) ?: return Result.failure()
            val workoutImagePath = inputData.getString(WORKOUT_IMAGE_PATH) ?: return Result.failure()

            /**
             * Read the answer and work area image files.
             */
            val answerImageBytes = MediaUtils.readImageFileByteArray(answerImagePath) ?: return handleRetry(attempt = retryAttempt)
            val workoutImageBytes = MediaUtils.readImageFileByteArray(workoutImagePath) ?: return handleRetry(attempt = retryAttempt)

            /**
             * Send the images to storage and get their media uri.
             */
            val answerImageUrl = mediaRepository.saveImage(imageByteArray = answerImageBytes).data ?: return handleRetry(attempt = retryAttempt)
            val workoutImageUrl = mediaRepository.saveImage(imageByteArray = workoutImageBytes).data ?: return handleRetry(attempt = retryAttempt)

            /**
             * Use AI to get the answer from the image.
             */
            val transcriptionResponse = withContext(Dispatchers.IO){
                artificialIntelligenceRepository.textExtractionFromImage(
                    GetTextFromImageRequestDTO(
                        url = answerImageUrl,
                    )
                ).last()
            }


            val studentAnswer = when(transcriptionResponse.status){
                ResultStatus.SUCCESS -> {
                    transcriptionResponse.data?.response
                }
                else -> {
                    null
                }
            }

            val comparison = compareResponseStrings(
                expected = expectedAnswer.toString(),
                actual = studentAnswer?.toString() ?: "",
                similarity = 0.9
            )

            val assessmentResult = NumeracyWordProblem(
                question = question,
                expectedAnswer = expectedAnswer,
                studentAnswer = studentAnswer,
                metadata = NumeracyOperationMetadata(
                    workAreaMediaUrl = workoutImageUrl,
                    answerMediaUrl = answerImageUrl,
                    passed = comparison.isMatch
                )
            )

            localDataSource.insertPendingNumeracyWordProblemResult(
                assessmentId = assessmentId,
                studentId = studentId,
                numeracyWordProblem = assessmentResult
            )

            MediaUtils.cleanUpMediaFile(path = workoutImagePath)
            MediaUtils.cleanUpMediaFile(path = answerImagePath)

            return Result.success()
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
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

    companion object {
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"
        const val QUESTION = "question"
        const val EXPECTED_ANSWER = "expected_answer"
        const val ANSWER_IMAGE_PATH = "answer_image_path"
        const val WORKOUT_IMAGE_PATH = "workout_image_path"
        const val WORK_NAME = "EvaluateNumeracyWordProblemWorker"
        const val MAX_RETRIES = 4
    }
}