package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubmitNumeracyCountAndMatchResultWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent{
    
    private val localDataSource: LocalDataSource by inject()
    private val assessmentRepository: AssessmentRepository by inject()
    
    override suspend fun doWork(): Result {
        try {
            val assessmentId = inputData.getString(ASSESSMENT_ID) ?: return Result.failure()
            val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
            
            
            val countAndMatchList = localDataSource.getPendingCountMatches(assessmentId = assessmentId, studentId = studentId).first()
            
            try {
                val submitCountAndMatchResultsResponse = assessmentRepository.assessNumeracyCountAndMatch(
                    assessmentId = assessmentId,
                    studentID = studentId,
                    countAndMatchList = countAndMatchList
                )

                when(submitCountAndMatchResultsResponse.status){
                    ResultStatus.INITIAL,
                    ResultStatus.LOADING -> {
                        return Result.retry()
                    }
                    ResultStatus.ERROR -> {
                        return Result.retry()
                    }
                    ResultStatus.SUCCESS -> {
                        localDataSource.clearPendingCountMatches(assessmentId = assessmentId, studentId = studentId)
                        return Result.success()
                    }
                }
                
            }catch (e: Exception){
                e.printStackTrace()
                return Result.retry()
            }
        }catch (e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
    }
    
    companion object {
        const val STUDENT_ID = "student_id"
        const val ASSESSMENT_ID = "assessment_id"
    }

}