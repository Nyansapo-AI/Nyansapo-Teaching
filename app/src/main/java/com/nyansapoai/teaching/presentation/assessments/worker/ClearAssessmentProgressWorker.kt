package com.nyansapoai.teaching.presentation.assessments.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.nyansapoai.teaching.data.local.LocalDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.getValue

class ClearAssessmentProgressWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : KoinComponent, CoroutineWorker(appContext, workerParams) {

    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
        val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()

        if (assessmentId.isEmpty() || studentId.isEmpty()) {
            Log.e(WORK_NAME, "Invalid input data, missing parameters: assessmentId: $assessmentId, studentId: $studentId")
            return Result.failure()
        }

        return try {
            localDataSource.clearAssessmentProgress(assessmentId, studentId)
            Result.success()
        } catch (e: Exception) {
            Log.e(WORK_NAME, "Error clearing assessment progress: ${e.message}")
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "ClearAssessmentProgressWorker"
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"

        fun buildRequest(assessmentId: String, studentId: String): OneTimeWorkRequest {
            val inputData = workDataOf(
                ASSESSMENT_ID to assessmentId,
                STUDENT_ID to studentId
            )

            return OneTimeWorkRequestBuilder<ClearAssessmentProgressWorker>()
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
        }
    }
}