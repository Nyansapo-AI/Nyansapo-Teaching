package com.nyansapoai.teaching.data.remote.ai

import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface ArtificialIntelligenceRepository {
    suspend fun recognizeImage(imageByteArray: ByteArray): Flow<Results<VisionRecognition>>
}