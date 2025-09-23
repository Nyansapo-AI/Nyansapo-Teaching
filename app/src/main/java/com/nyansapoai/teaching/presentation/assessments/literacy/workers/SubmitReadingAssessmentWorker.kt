package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitReadingAssessmentWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val assessmentRepository: AssessmentRepository by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        val retryAttempt = runAttemptCount
        val assessmentId = inputData.getString("assessment_id")
        val studentId = inputData.getString("student_id")

        if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty()) {
            Log.e("SubmitReadingAssessmentWorker", "Missing assessmentId or studentId")
            return Result.failure()
        }

        return try {
            val pending = localDataSource.getPendingReadingResults(
                assessmentId = assessmentId,
                studentId = studentId
            ).first()

            if (pending.isEmpty()) {
                Log.i("SubmitReadingAssessmentWorker", "No pending results to submit")
                return Result.success()
            }

            val grouped = pending.groupBy { it.assessmentId to it.studentId }

            val allSucceeded = grouped.all { (key, results) ->
                val (groupAssessmentId, groupStudentId) = key
                val readingAssessmentResult = results.map { it.readingAssessmentResult }
                try {
                    val response = assessmentRepository.assessReadingAssessment(
                        assessmentId = groupAssessmentId,
                        studentID = groupStudentId,
                        readingAssessmentResults = readingAssessmentResult
                    )
                    when (response.status) {
                        ResultStatus.SUCCESS -> {
                            localDataSource.markResultsAsSubmitted(
                                assessmentId = groupAssessmentId,
                                studentId = groupStudentId
                            )
                            localDataSource.deleteSubmittedResults(
                                assessmentId = groupAssessmentId,
                                studentId = groupStudentId
                            )
                            true
                        }
                        ResultStatus.ERROR -> {
                            Log.e("SubmitReadingAssessmentWorker", "Submission failed for $groupAssessmentId/$groupStudentId: ${response.message}")
                            false
                        }
                        else -> true
                    }
                } catch (e: Exception) {
                    Log.e("SubmitReadingAssessmentWorker", "Exception for $groupAssessmentId/$groupStudentId", e)
                    false
                }
            }

            if (allSucceeded) Result.success() else Result.retry()
        } catch (e: Exception) {
            Log.e("SubmitReadingAssessmentWorker", "Exception in doWork", e)
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
        const val WORK_NAME = "SubmitReadingAssessmentWorker"
        const val MAX_RETRIES = 4
    }
}