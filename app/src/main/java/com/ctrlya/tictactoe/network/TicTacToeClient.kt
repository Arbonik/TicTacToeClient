package com.ctrlya.tictactoe.network

import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.player.NetworkPlayer
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class TicTacToeClient : KtorClient() {
    private val BASE_URL = URLBuilder(host = "89.223.123.239", port = 8888)

    suspend fun createRoom() = on<CreateRoomResponse> {
        client.post(BASE_URL.appendPathSegments("createroom").build())
    }

    suspend fun allRooms() = on<RoomsResponse> {
        client.get(BASE_URL.appendPathSegments("rooms").build())
    }

    suspend fun connectToGame(id: String, networkPlayer: NetworkPlayer) {
        client.webSocket(BASE_URL.appendPathSegments(id).buildString()) {
            launch {
                networkPlayer.outgoingChannel.consumeEach {event ->
                    when (event){
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
            }
        }
    }
}