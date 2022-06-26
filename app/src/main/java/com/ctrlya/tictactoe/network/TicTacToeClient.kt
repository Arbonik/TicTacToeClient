package com.ctrlya.tictactoe.network

import android.util.Log
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.player.NetworkPlayer
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.concurrent.fixedRateTimer

class TicTacToeClient : KtorClient() {
    private val HOST = "mysterious-tor-86270.herokuapp.com"
    private val BASE_URL =
        URLBuilder(host = HOST, pathSegments = listOf("rooms")).build()
    private val CREATE_ROOM_URL =
        URLBuilder(host = HOST, pathSegments = listOf("createroom")).build()

    fun urlToSocket(id: String): String {
        return URLBuilder(
            protocol = URLProtocol.WSS,
            host = HOST,
            pathSegments = listOf(id)
        ).buildString()
    }

    suspend fun createRoom(gameSettings: BattlefieldSettings) = on<CreateRoomResponse> {
        client.post(CREATE_ROOM_URL) {
            setBody(
                Json.encodeToString(gameSettings)
            )
        }
    }

    suspend fun allRooms() = on<RoomsResponse> {
        client.get(BASE_URL)
    }

    suspend fun connectToGame(id: String, ctrlProtocol: CtrlProtocol, sharedFlow: SharedFlow<String>) {
        client.webSocket(urlToSocket(id)) {
            launch {
                sharedFlow.collectLatest { frame ->
                    outgoing.send(Frame.Text(frame))
                }
            }
            for (frame in incoming) {
                if (frame is Frame.Text)
                    messageReceive(frame, ctrlProtocol)
                else {
                    Log.d("AAA", frame.data.toString())
                }
            }
        }
    }
}