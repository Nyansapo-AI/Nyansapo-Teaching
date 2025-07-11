package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class LiteracyAssessmentsMonitorWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val workManager: WorkManager by inject()

    override suspend fun doWork(): Result {
        try {

            val assessmentType = inputData.getString("assessment_type") ?: return Result.failure()
            val workIdStrings = inputData.getStringArray(assessmentType) ?: return Result.failure()

            val worksId = workIdStrings.map { UUID.fromString(it) }

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
                Result.retry()
            }

        }catch (e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
    }
}