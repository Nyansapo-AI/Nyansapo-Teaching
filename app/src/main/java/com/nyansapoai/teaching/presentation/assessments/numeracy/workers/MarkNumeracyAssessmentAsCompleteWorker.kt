package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MarkNumeracyAssessmentAsCompleteWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        try {
            val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
            val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()

            if (studentId.isEmpty() || assessmentId.isEmpty()){
                return Result.failure()
            }

            localDataSource.completeAssessment(
                studentId = studentId,
                assessmentId = assessmentId,
                isCompleted = true
            )


            return Result.success()
        }catch (e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "MarkNumeracyAssessmentAsCompleteWorker"
        const val STUDENT_ID = "student_id"
        const val ASSESSMENT_ID = "assessment_id"
    }

}