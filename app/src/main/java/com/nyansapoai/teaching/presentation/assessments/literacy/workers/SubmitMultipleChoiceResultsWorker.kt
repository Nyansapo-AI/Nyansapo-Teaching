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

class SubmitMultipleChoiceResultsWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val localDataSource: LocalDataSource by inject()
    private val assessmentRepository : AssessmentRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()
            val studentId = inputData.getString("student_id") ?: return Result.failure()

            val pending = localDataSource.getPendingMultipleChoicesResults(assessmentId = assessmentId, studentId = studentId).first()

            if (pending.isEmpty()) return Result.success()

            val grouped = pending.groupBy { it.assessmentId to it.studentId }

            var hasFailure = false

            grouped.forEach { (key, results) ->
                val (assessmentId, studentId) = key
                val pendingResults = results.map { it.multipleChoicesResult }

                val response = assessmentRepository.assessMultipleChoiceQuestions(
                    assessmentId = assessmentId,
                    studentID = studentId,
                    multipleChoiceQuestions = pendingResults
                )

                when(response.status){
                    ResultStatus.INITIAL,
                    ResultStatus.LOADING -> {}
                    ResultStatus.SUCCESS -> {

                        localDataSource.markMultipleChoicesResultsAsSubmitted(
                            studentId = studentId,
                            assessmentId = assessmentId
                        )

                    }
                    ResultStatus.ERROR -> {
                        hasFailure = true
                    }

                }
            }

            if(hasFailure) Result.retry() else Result.success()

            }catch (e: Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}