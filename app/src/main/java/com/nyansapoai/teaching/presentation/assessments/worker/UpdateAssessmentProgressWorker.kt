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

class UpdateAssessmentProgressWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : KoinComponent, CoroutineWorker(appContext, workerParams){

    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
        val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
        val currentIndex = inputData.getInt(CURRENT_INDEX, 0)
        val currentAssessmentLevel = inputData.getString(CURRENT_ASSESSMENT_LEVEL) ?: return Result.failure()


        if (assessmentId.isEmpty() || studentId.isEmpty() || currentAssessmentLevel.isEmpty()) {
            Log.e(WORK_NAME, "Invalid input data, missing parameters: assessmentId: $assessmentId, studentId: $studentId,level: $currentAssessmentLevel")
            return Result.failure()
        }

        try {
            localDataSource.insertAssessmentProgress(
                assessmentId = assessmentId,
                studentId = studentId,
                currentIndex = currentIndex,
                level = currentAssessmentLevel
            )

            return Result.success()
        }catch (e: Exception){
            Log.e(WORK_NAME, "Error updating assessment progress: ${e.message}")
            return Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "UpdateAssessmentProgressWorker"
        const val ASSESSMENT_ID = "assessment_id"
        const val STUDENT_ID = "student_id"
        const val CURRENT_INDEX = "current_index"
        const val CURRENT_ASSESSMENT_LEVEL = "current_assessment_level"


        fun buildRequest(assessmentId: String, studentId: String, currentIndex: Int, currentAssessmentLevel: String): OneTimeWorkRequest {

            val inputData = workDataOf(
                ASSESSMENT_ID to assessmentId,
                STUDENT_ID to studentId,
                CURRENT_INDEX to currentIndex,
                CURRENT_ASSESSMENT_LEVEL to currentAssessmentLevel
            )

            return OneTimeWorkRequestBuilder<UpdateAssessmentProgressWorker>()
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
        }


    }

}