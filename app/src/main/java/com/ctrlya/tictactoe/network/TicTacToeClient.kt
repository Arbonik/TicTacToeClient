package com.ctrlya.tictactoe.network

import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import io.ktor.client.request.*
import io.ktor.http.*

class TicTacToeClient : KtorClient() {
    private val BASE_URL = URLBuilder(host = "172.20.10.5", port = 8888)
    suspend fun createRoom() = on<CreateRoomResponse> {
        client.post(BASE_URL.appendPathSegments("createroom").build())
    }
}