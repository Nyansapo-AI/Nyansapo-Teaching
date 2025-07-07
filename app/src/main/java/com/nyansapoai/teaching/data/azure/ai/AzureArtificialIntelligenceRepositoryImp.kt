package com.nyansapoai.teaching.data.azure.ai

import com.nyansapoai.teaching.data.network.ApiHelper
import com.nyansapoai.teaching.data.network.Http
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.domain.dto.ai.GetTextFromImageRequestDTO
import com.nyansapoai.teaching.domain.models.ai.SpeechRecognition
import com.nyansapoai.teaching.domain.models.ai.TextFromImageResult
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.utils.Results
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.content.ByteArrayContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class AzureArtificialIntelligenceRepositoryImp(
    val apiHelper: ApiHelper
): ArtificialIntelligenceRepository {
    override suspend fun recognizeImage(imageByteArray: ByteArray): Flow<Results<VisionRecognition>> {
        TODO("Not yet implemented")
    }

    override suspend fun textExtractionFromImage(request: GetTextFromImageRequestDTO): Flow<Results<VisionRecognition>> {
        return flow {
            try {

                val response = Http().azureVisionClient
                    .post("computervision/imageanalysis:analyze?features=caption,read&model-version=latest&language=en&api-version=2024-02-01") {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }


                if (response.status != HttpStatusCode.OK){
                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<String>()
                    }
                    emit(Results.error(msg = apiResponse.message ?: "Unknown Error"))
                }else {

                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<TextFromImageResult>()
                    }

                    apiResponse.data?.let { data ->
                        data.readResult.blocks.first().lines.first().text.toIntOrNull()?.let {
                            emit(
                                Results.success(
                                    data = VisionRecognition(
                                        url = request.url,
                                        response = it
                                    )
                                )
                            )
                        }

                    } ?: run {
                        emit(Results.error(msg = "No text found in image."))
                    }
                }

            }catch (e: Exception) {
                emit(Results.error(msg = e.message ?: "Unknown Error"))
            }
        }
    }

    override suspend fun getTextFromAudio(audioByteArray: ByteArray): Flow<Results<SpeechRecognition>> {
        return flow {
            val response = try {
                Http().azureSpeechClient
                    .post("speech/recognition/conversation/cognitiveservices/v1?cid=9e2e53a9-10c1-43fc-8780-45883b57a726") {
                        header(HttpHeaders.ContentType, "audio/wav")
                        setBody(ByteArrayContent(audioByteArray, contentType = ContentType.Audio.Any))
                    }
            } catch (e: Exception) {
                emit(Results.error<SpeechRecognition>(msg = e.message ?: "API request failed"))
                return@flow
            }

            if (response.status != HttpStatusCode.OK) {
                val errorMessage = apiHelper.safeApiCall(response.status) {
                    response.body<String>()
                }.message ?: "Unknown Error"
                emit(Results.error(msg = errorMessage))
                return@flow
            }

            val apiResponse = apiHelper.safeApiCall(response.status) {
                response.body<SpeechRecognition>()
            }

            apiResponse.data?.let { data ->
                emit(Results.success(data = data))
            } ?: emit(Results.error(msg = "No text found in audio."))
        }.catch { e ->
            // This properly handles Flow exception transparency
            emit(Results.error(msg = e.message ?: "Unknown Error"))
        }
    }
}