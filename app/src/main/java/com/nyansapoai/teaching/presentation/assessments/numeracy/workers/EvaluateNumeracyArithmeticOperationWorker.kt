package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.domain.dto.ai.GetTextFromImageRequestDTO
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationMetadata
import com.nyansapoai.teaching.presentation.assessments.literacy.components.compareResponseStrings
import com.nyansapoai.teaching.presentation.common.media.MediaUtils
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


/**
 * This worker evaluates a numeracy operation by comparing the expected answer with the student's answer.
 */

class EvaluateNumeracyArithmeticOperationWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository by inject()
    private val mediaRepository: MediaRepository by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {

        val retryAttempt = runAttemptCount

        try {
            /**
             * get the input data from the worker parameters
             */
            val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
            val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
            val operationType = inputData.getString(OPERATION_TYPE) ?: return Result.failure()
            val operationNumber1 = inputData.getInt(OPERATION_NUMBER1, 0)
            val operationNumber2 = inputData.getInt(OPERATION_NUMBER2, 0)
            val expectAnswer = inputData.getInt(EXPECTED_ANSWER, 0)
            val answerImageFilePath = inputData.getString(ANSWER_IMAGE_PATH) ?: return Result.failure()
            val workoutImagePath = inputData.getString(WORKOUT_IMAGE_PATH) ?: return Result.failure()

            /**
             * Read the image files.
             */
            val answerImageBytes = MediaUtils.readImageFileByteArray(answerImageFilePath) ?: return Result.failure()
            val workoutImageByte = MediaUtils.readImageFileByteArray(workoutImagePath) ?: return Result.failure()

            /**
             * Send the images to storage and get their media uri.
             */
            val answerImageUrl = mediaRepository.saveImage(imageByteArray = answerImageBytes).data ?: return handleRetry(attempt = retryAttempt)
            val workoutImageUri = mediaRepository.saveImage(imageByteArray = workoutImageByte).data ?: return handleRetry(attempt = retryAttempt)

            /**
             * Extract text from the answer image using AI.
             */
            val transcriptionResponse = withContext(Dispatchers.IO) {
                artificialIntelligenceRepository.textExtractionFromImage(request = GetTextFromImageRequestDTO(url = answerImageUrl)).last()
            }
            val studentAnswer = when(transcriptionResponse.status){
                ResultStatus.SUCCESS -> { transcriptionResponse.data?.response}
                else-> {
                    null
                }
            }

            /**
             * Compare the expected answer with the student's answer.
             * If the student's answer is null, it means the AI could not extract any text from the image.
             */
            val comparison = compareResponseStrings(
                expected = expectAnswer.toString(),
                actual = studentAnswer?.toString() ?: "",
                similarity = 0.9
            )

            val operationResult = NumeracyArithmeticOperation(
                type = operationType,
                expected_answer = expectAnswer,
                student_answer = studentAnswer,
                operationNumber1 = operationNumber1,
                operationNumber2 = operationNumber2,
                metadata = NumeracyOperationMetadata(
                    workAreaMediaUrl = workoutImageUri,
                    answerMediaUrl = answerImageUrl,
                    passed = comparison.isMatch
                )
            )

            /**
             * Insert the operation result into the local database.
             */
            localDataSource.insertPendingNumeracyOperation(
                assessmentId = assessmentId,
                studentId = studentId,
                numeracyArithmeticOperation = operationResult
            )

            MediaUtils.cleanUpMediaFile(path = answerImageFilePath)
            MediaUtils.cleanUpMediaFile(path = workoutImagePath)

            return Result.success()

        }catch (e:Exception){
            Log.e(WORK_NAME, "Error evaluating numeracy operation: ${e.message}", e)
            return handleRetry(attempt = retryAttempt)
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
        const val WORK_NAME = "EvaluateNumeracyOperationWorker"
        const val ANSWER_IMAGE_PATH = "answer_image_path"
        const val WORKOUT_IMAGE_PATH = "workout_image_path"
        const val EXPECTED_ANSWER = "expected_answer"
        const val OPERATION_NUMBER1 = "operation_number1"
        const val OPERATION_NUMBER2 = "operation_number2"
        const val OPERATION_TYPE = "operation_type"
        const val STUDENT_ID = "student_id"
        const val ASSESSMENT_ID = "assessment_id"

        const val MAX_RETRIES = 3 // Maximum number of retries for the worker
    }
}