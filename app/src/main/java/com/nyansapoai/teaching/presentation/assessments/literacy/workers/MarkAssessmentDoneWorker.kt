package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class MarkAssessmentDoneWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent{

    private val assessmentRepository : AssessmentRepository by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        return try {
            val studentId = inputData.getString("student_id") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()

            val response = assessmentRepository.markAssessmentDone(assessmentId = assessmentId, studentId = studentId)

            if(response.status.name == "SUCCESS"){
                Result.success()
            } else {
                Result.retry()
            }

        }catch (e: Exception){
            e.printStackTrace()
            Result.failure()
        }
    }

}