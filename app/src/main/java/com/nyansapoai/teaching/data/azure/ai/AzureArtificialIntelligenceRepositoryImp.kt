package com.nyansapoai.teaching.data.azure.ai

import com.nyansapoai.teaching.data.network.ApiHelper
import com.nyansapoai.teaching.data.network.Http
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.domain.dto.ai.GetTextFromImageRequestDTO
import com.nyansapoai.teaching.domain.models.ai.TextFromImageResult
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.utils.Results
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
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

                val response = Http().azureClient
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

}