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

class SubmitMultipleChoiceResultsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val localDataSource: LocalDataSource by inject()
    private val assessmentRepository: AssessmentRepository by inject()

    override suspend fun doWork(): Result {
        val assessmentId = inputData.getString("assessment_id")
        val studentId = inputData.getString("student_id")

        if (assessmentId.isNullOrEmpty() || studentId.isNullOrEmpty()) {
            Log.e(WORK_NAME, "Missing assessmentId or studentId")
            return Result.failure()
        }

        return try {
            val pending = localDataSource.getPendingMultipleChoicesResults(
                assessmentId = assessmentId,
                studentId = studentId
            ).first()

            if (pending.isEmpty()) {
                Log.i(WORK_NAME, "No pending results to submit")
                return Result.success()
            }

            val grouped = pending.groupBy { it.assessmentId to it.studentId }

            val allSucceeded = grouped.all { (key, results) ->
                val (groupAssessmentId, groupStudentId) = key
                val pendingResults = results.map { it.multipleChoicesResult }
                try {
                    val response = assessmentRepository.assessMultipleChoiceQuestions(
                        assessmentId = groupAssessmentId,
                        studentID = groupStudentId,
                        multipleChoiceQuestions = pendingResults
                    )
                    when (response.status) {
                        ResultStatus.SUCCESS -> {
                            localDataSource.markMultipleChoicesResultsAsSubmitted(
                                studentId = groupStudentId,
                                assessmentId = groupAssessmentId
                            )

                            localDataSource.clearSubmittedMultipleChoicesResults(
                                assessmentId = groupAssessmentId,
                                studentId = groupStudentId
                            )

                            true
                        }
                        ResultStatus.ERROR -> {
                            Log.e(WORK_NAME, "Submission failed for $groupAssessmentId/$groupStudentId: ${response.message}")
                            false
                        }
                        else -> true
                    }
                } catch (e: Exception) {
                    Log.e(WORK_NAME, "Exception for $groupAssessmentId/$groupStudentId", e)
                    false
                }
            }

            if (allSucceeded) Result.success() else Result.failure()
        } catch (e: Exception) {
            Log.e(WORK_NAME, "Exception in doWork", e)
            Result.failure()
        }
    }
    companion object {
        const val WORK_NAME = "SubmitMultipleChoiceResultsWorker"
        const val MAX_RETRIES = 4
    }
}