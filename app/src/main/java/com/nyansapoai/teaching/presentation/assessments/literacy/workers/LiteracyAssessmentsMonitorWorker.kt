package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker.Companion.MAX_RETRIES
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker.Companion.WORK_NAME
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class LiteracyAssessmentsMonitorWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val workManager: WorkManager by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        val retryAttemptCount = runAttemptCount
        try {


            val studentId = inputData.getString("student_id") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()
            val assessmentType = inputData.getString("assessment_type") ?: return Result.failure()

            val requestsId = localDataSource.getLiteracyAssessmentWorkerRequests(assessmentId = assessmentId, studentId = studentId, type = assessmentType).first()


            val worksId = requestsId.map { UUID.fromString(it) }

            if (worksId.isEmpty()){
                return Result.success()
            }

            val allWorkInfo = worksId.mapNotNull { id ->
                try {
                    workManager.getWorkInfoById(id).get()
                }catch (e: Exception){
                    null
                }
            }

            val allFinished = allWorkInfo.size == worksId.size && allWorkInfo.all { it.state.isFinished }

            return if (allFinished){
                Result.success()
            }else {
                handleRetry(attempt = retryAttemptCount)
            }

        }catch (e: Exception){
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
        const val MAX_RETRIES = 4
        const val WORK_NAME = "LiteracyAssessmentsMonitorWorker"
    }
}