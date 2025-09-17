package com.nyansapoai.teaching.presentation.assessments.numeracy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent

class EvaluateNumeracyCountAndMatchWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent{
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}