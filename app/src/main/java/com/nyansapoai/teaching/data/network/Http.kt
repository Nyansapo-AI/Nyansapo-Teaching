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

    val azureVisionBaseUrl = BuildConfig.AZURE_BASE_URL
    val azureVisionSubscriptionKey = BuildConfig.AZURE_SUBSCRIPTION_KEY

    val azureSpeechBaseUrl = BuildConfig.AZURE_SPEECH_BASE_URL
    val azureSpeechSubscriptionKey = BuildConfig.AZURE_SPEECH_SUBSCRIPTION_KEY

    val client by lazy {
        HttpClient(httpClient()) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                level = LogLevel.INFO
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


    val azureVisionClient by lazy {
        HttpClient(httpClient()) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                level = LogLevel.INFO

                sanitizeHeader {  header -> header == "Ocp-Apim-Subscription-Key" }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 6000
                socketTimeoutMillis = 6000
                connectTimeoutMillis = 6000
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
                url(azureVisionBaseUrl)
                header("Ocp-Apim-Subscription-Key", azureVisionSubscriptionKey)
            }

        }
    }

    val azureSpeechClient by lazy {
        HttpClient(httpClient()) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message.take(1000))
                        }
                    }
                level = LogLevel.INFO

                sanitizeHeader { header -> header == "Ocp-Apim-Subscription-Key" }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 6000
                socketTimeoutMillis = 6000
                connectTimeoutMillis = 6000
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
                url(azureSpeechBaseUrl)
                header("Ocp-Apim-Subscription-Key", azureSpeechSubscriptionKey)
            }

        }
    }


}


fun httpClient(): HttpClientEngine {
    return OkHttp.create {
    }
}