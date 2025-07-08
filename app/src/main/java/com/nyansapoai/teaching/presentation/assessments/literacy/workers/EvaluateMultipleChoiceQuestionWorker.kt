package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EvaluateMultipleChoiceQuestionWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext = appContext, params = params), KoinComponent {

    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        return try {
            val studentId = inputData.getString("studentId") ?: return Result.failure()
            val assessmentId = inputData.getString("assessmentId") ?: return Result.failure()
            val question = inputData.getString("question") ?: return Result.failure()
            val options = inputData.getStringArray("options") ?: return Result.failure()
            val studentAnswer = inputData.getString("studentAnswer") ?: return Result.failure()
            val passed = inputData.getBoolean("passed", false)


            localDataSource.insertPendingMultipleChoicesResult(
                assessmentId = assessmentId,
                studentId = studentId,
                question = question,
                options = options.toList(),
                studentAnswer = studentAnswer,
                passed = passed,
                timestamp = Clock.System.now().epochSeconds.toInt(),
                isPending = true
            )

            Result.success()
        }catch (e: Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}