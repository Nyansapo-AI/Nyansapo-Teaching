package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker.Companion.MAX_RETRIES
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker.Companion.WORK_NAME
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MarkLiteracyAssessmentWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val assessmentRepository : AssessmentRepository by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        return try {
            val retryAttempt = runAttemptCount
            val studentId = inputData.getString("student_id") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()

            var hasFailure = false

            val response = assessmentRepository.markLiteracyAssessmentAsComplete(assessmentId = assessmentId, studentId = studentId)

            when(response.status){
                ResultStatus.ERROR -> {
                    hasFailure = true
                }
                ResultStatus.SUCCESS -> {
                    localDataSource.clearLiteracyAssessmentRequests(assessmentId = assessmentId, studentId = studentId, type = "reading_assessment")
                    localDataSource.clearLiteracyAssessmentRequests(assessmentId = assessmentId, studentId = studentId, type = "multiple_choices")
                }
                else -> {}
            }

            if (hasFailure) handleRetry(attempt = retryAttempt) else Result.success()

        }catch (e: Exception){
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

    companion object {
        const val WORK_NAME = "MarkLiteracyAssessmentWorker"
        const val MAX_RETRIES = 4
    }
}