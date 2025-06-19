package com.nyansapoai.teaching.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.header
import com.nyansapoai.teaching.BuildConfig


class Http() {

    val azureBaseUrl = BuildConfig.AZURE_BASE_URL
    val azureSubscriptionKey = BuildConfig.AZURE_SUBSCRIPTION_KEY

    val client by lazy {
        HttpClient(httpClient()) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                socketTimeoutMillis = 60000
                connectTimeoutMillis = 60000
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            defaultRequest {
                url(httpUrlBuilder())
            }
        }
    }


    val azureClient by lazy {
        HttpClient(httpClient()) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                socketTimeoutMillis = 60000
                connectTimeoutMillis = 60000
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            defaultRequest {
                url(azureBaseUrl)
                header("Ocp-Apim-Subscription-Key", azureSubscriptionKey)
            }

        }
    }


}


fun httpClient(): HttpClientEngine {
    return OkHttp.create {
    }
}