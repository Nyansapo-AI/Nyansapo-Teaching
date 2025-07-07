package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
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
): CoroutineWorker(appContext, params), KoinComponent {

    private val assessmentRepository: AssessmentRepository by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        return try {
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()
            val studentId = inputData.getString("student_id") ?: return Result.failure()

            val pending = localDataSource.getPendingReadingResults(assessmentId = assessmentId, studentId = studentId).first()

            if (pending.isEmpty()) return Result.success()

            val grouped = pending.groupBy { it.assessmentId to it.studentId }

            var hasFailure: Boolean = false

            grouped.forEach { (key, results) ->
                val (assessmentId, studentId) = key
                val readingAssessmentResult = results.map{ it.readingAssessmentResult }

                val response = assessmentRepository.assessReadingAssessment(assessmentId = assessmentId, studentID = studentId, readingAssessmentResults = readingAssessmentResult)

                when(response.status){
                    ResultStatus.INITIAL ,
                    ResultStatus.LOADING -> {}
                    ResultStatus.SUCCESS -> {
                        localDataSource.markResultsAsSubmitted(assessmentId = assessmentId, studentId = studentId)

                        localDataSource.deleteSubmittedResults(assessmentId = assessmentId, studentId = studentId)
                    }
                    ResultStatus.ERROR -> {
                        hasFailure = true
                    }

                }
            }

            if (hasFailure) Result.retry() else Result.success()

        }catch (e: Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}