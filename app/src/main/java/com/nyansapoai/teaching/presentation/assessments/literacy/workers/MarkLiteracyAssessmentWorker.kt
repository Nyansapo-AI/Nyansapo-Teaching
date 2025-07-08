package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MarkLiteracyAssessmentWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val assessmentRepository : AssessmentRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            val studentId = inputData.getString("student_id") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()

            var hasFailure = false

            val response = assessmentRepository.markLiteracyAssessmentAsComplete(assessmentId = assessmentId, studentId = studentId)

            when(response.status){
                ResultStatus.ERROR -> {
                    hasFailure = true
                }
                else -> {}
            }

            if (hasFailure) Result.retry() else Result.success()

        }catch (e: Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}