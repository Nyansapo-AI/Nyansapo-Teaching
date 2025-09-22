package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

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
import kotlin.getValue

class SubmitNumeracyWordProblemResultsWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val assessmentRepository: AssessmentRepository by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
        val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()

        if (assessmentId.isEmpty() || studentId.isEmpty()) {
            Log.e("SubmitNumeracyWordProblemResultsWorker", "Missing assessmentId or studentId")
            return Result.failure()
        }

        return try {

            val pending = localDataSource.getPendingNumeracyWordProblems(
                assessmentId = assessmentId,
                studentId = studentId
            ).first()

            if (pending.isEmpty()) {
                Log.i("SubmitNumeracyWordProblem", "No pending results to submit")
                return Result.success()
            }


            val submitNumeracyWordProblemResultsResponse = assessmentRepository.assessNumeracyWordProblem(
                assessmentId = assessmentId,
                studentID = studentId,
                wordProblemList = pending
            )

            when(submitNumeracyWordProblemResultsResponse.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING,
                ResultStatus.ERROR -> {
                    return Result.retry()
                }
                ResultStatus.SUCCESS -> {
                    localDataSource.clearPendingNumeracyWordProblems(assessmentId = assessmentId, studentId = studentId)
                    return Result.success()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"
    }

}