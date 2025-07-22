package com.nyansapoai.teaching.presentation.assessments.literacy.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker.Companion.MAX_RETRIES
import com.nyansapoai.teaching.presentation.assessments.numeracy.workers.EvaluateNumeracyArithmeticOperationWorker.Companion.WORK_NAME
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import kotlin.text.compareTo

class LiteracyAssessmentsMonitorWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val workManager: WorkManager by inject()
    private val localDataSource: LocalDataSource by inject()

    override suspend fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
       /*
        createNotificationChannel(notificationManager = notificationManager)
        setForeground(createForegroundInfo("Monitoring literacy assessments..."))

        */
        val retryAttemptCount = runAttemptCount
        try {

            val studentId = inputData.getString("student_id") ?: return Result.failure()
            val assessmentId = inputData.getString("assessment_id") ?: return Result.failure()
            val assessmentType = inputData.getString("assessment_type") ?: return Result.failure()

            val requestsId = localDataSource.getLiteracyAssessmentWorkerRequests(assessmentId = assessmentId, studentId = studentId, type = assessmentType).first()



            val worksId = requestsId.map { UUID.fromString(it) }

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

            val completedTasks = allWorkInfo.count{it.state.isFinished}
            val totalTasks = allWorkInfo.size
            val progressPercent = if (totalTasks > 0)(completedTasks * 100) / totalTasks else 0

            /*
            setForeground(createForegroundInfo(message = "Progress: $progressPercent% of ${totalTasks} tasks completed."))


             */
            val allFinished = allWorkInfo.size == worksId.size && allWorkInfo.all { it.state.isFinished }

            return if (allFinished){
                /*
                setForeground(createForegroundInfo("Assessment evaluation completed!"))

                 */
                Result.success()
            }else {
                /*
                setForeground(createForegroundInfo("Scheduling retry ${retryAttemptCount + 1}..."))

                 */
                Result.retry()
            }

        }catch (e: Exception){
            /*
            setForeground(createForegroundInfo(message = "An error occurred: ${e.message?.take(20)}"))

             */
            e.printStackTrace()
            return Result.failure()
        }
    }

    private fun handleRetry(attempt: Int): Result {
        return if (attempt >= MAX_RETRIES) {
            Log.e(WORK_NAME, "Max retries reached")
            Result.failure()
        } else {
            Log.d(WORK_NAME, "Scheduling retry ${attempt + 1}")
            Result.retry()
        }
    }

    /*
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Progress Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows progress of background tasks"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createForegroundInfo(message: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Background Task")
            .setContentText(message)
            .setSmallIcon(R.drawable.nyansapo_ai_icon_small)
            .setOngoing(true)
            .build()


        // Specify foreground service type when creating ForegroundInfo
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                2 // ForegroundServiceType.DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }

//        return  ForegroundInfo(NOTIFICATION_ID, notification)
    }*/

    companion object {
        const val MAX_RETRIES = 4
        const val WORK_NAME = "LiteracyAssessmentsMonitorWorker"
        const val CHANNEL_ID = "progress_channel"
        const val NOTIFICATION_ID = 1
    }
}