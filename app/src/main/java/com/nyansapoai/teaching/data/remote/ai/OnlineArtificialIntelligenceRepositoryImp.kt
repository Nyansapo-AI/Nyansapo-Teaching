package com.nyansapoai.teaching.data.remote.ai

import android.net.http.HttpException
import com.nyansapoai.teaching.data.network.ApiHelper
import com.nyansapoai.teaching.data.network.Http
import com.nyansapoai.teaching.domain.models.ai.VisionRecognition
import com.nyansapoai.teaching.utils.Results
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import okhttp3.internal.format

class OnlineArtificialIntelligenceRepositoryImp(
    val apiHelper: ApiHelper,
): ArtificialIntelligenceRepository {

    override suspend fun recognizeImage(imageByteArray: ByteArray): Flow<Results<VisionRecognition>> {
        return flow {
            try {
                val response = Http().client
                    .post("/vision_recognition"){
                        setBody(
                            MultiPartFormDataContent(
                                formData {
                                    append(
                                        "uploaded_file",
                                        imageByteArray,
                                        Headers.build {
                                            append(HttpHeaders.ContentType, "image/png")
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"screenshot.png\"",
                                            )
                                        },
                                    )
                                }
                            )
                        )
                    }


                if (response.status != HttpStatusCode.OK){
                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<String>()
                    }
                    emit(Results.error(msg = apiResponse.message ?: "Unknown Error"))
                }else {
                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<VisionRecognition>()
                    }
                    emit(apiResponse)
                }


            }catch (e: Exception){
                emit(Results.error(e.message ?: "Unknown Error reading the input"))
            }
        }.flowOn(Dispatchers.IO)
    }
}