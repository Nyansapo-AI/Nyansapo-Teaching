package com.nyansapoai.teaching.data.network

import com.nyansapoai.teaching.utils.Results
import io.ktor.http.HttpStatusCode
import org.koin.core.component.KoinComponent

class ApiHelper() : KoinComponent {

    suspend fun <T : Any> safeApiCall(
        statusCode: HttpStatusCode,
        apiCall: suspend () -> T,
    ): Results<T> {
        return try {
            when (statusCode) {

                HttpStatusCode.Unauthorized -> {
                    Results.error("Unauthorized access")
                }

                HttpStatusCode.Locked -> {
                    Results.error("You are out of tokens")
                }

                else -> {
                    val result = apiCall.invoke()
                    Results.success(result)
                }
            }
        } catch (throwable: Throwable) {
            Results.error("Server Error ${throwable.message}")
        }
    }
}