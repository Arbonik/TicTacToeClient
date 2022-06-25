package com.ctrlya.tictactoe.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

abstract class KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(WebSockets)
    }

    protected suspend inline fun <reified T> on(
        crossinline request: suspend () -> HttpResponse
    ): Result<T> =
        try {
            Result.success(
                withContext(Dispatchers.IO) {
                    request().body<T>()
                }
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
}