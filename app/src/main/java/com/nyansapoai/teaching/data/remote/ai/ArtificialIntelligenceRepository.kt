package com.nyansapoai.teaching.data.remote.ai

import com.nyansapoai.teaching.domain.dto.ai.GetTextFromImageRequestDTO
import com.nyansapoai.teaching.domain.models.ai.SpeechRecognition
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface ArtificialIntelligenceRepository {
    suspend fun recognizeImage(imageByteArray: ByteArray): Flow<Results<VisionRecognition>>

    suspend fun textExtractionFromImage(request: GetTextFromImageRequestDTO): Flow<Results<VisionRecognition>>

    suspend fun getTextFromAudio(audioByteArray: ByteArray): Flow<Results<SpeechRecognition>>

}