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
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TicTacToeClient : KtorClient() {
    private val HOST = "mysterious-tor-86270.herokuapp.com"
    private val BASE_URL =
        URLBuilder(host = HOST, pathSegments = listOf("rooms")).build()
    private val CREATE_ROOM_URL =
        URLBuilder(host = HOST, pathSegments = listOf("createroom")).build()
    private val BASE_URL_Connect = URLBuilder(host = HOST)

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

    suspend fun connectToGame(id: String, networkPlayer: NetworkPlayer) {
        client.webSocket(BASE_URL_Connect.appendPathSegments(id).buildString()) {
            launch {
                networkPlayer.outgoingChannel.consumeEach { event ->
                    when (event) {
                        GameEvent.CREATED -> {}
                        GameEvent.DRAW -> {}
                        GameEvent.END -> {}
                        GameEvent.INIT -> {}
                        is GameEvent.Start -> {}
                        is GameEvent.Turn -> {
                            outgoing.send(Frame.Text(sendCtrlProtocol(event.point)))
                        }
                        is GameEvent.Win -> {}
                    }
                }
            }
            for (frame in incoming) {
                if (frame is Frame.Text)
                    messageReceive(frame, networkPlayer)
                else {
                    Log.d("AAA", frame.data.toString())
                }
            }
        }
    }
}