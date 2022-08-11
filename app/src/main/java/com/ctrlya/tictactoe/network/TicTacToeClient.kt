package com.ctrlya.tictactoe.network

import android.os.Build
import android.util.Log
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TicTacToeClient : KtorClient() {
    private val HOST = "192.168.43.240"//"mysterious-tor-86270.herokuapp.com"
    private val All_ROOMS =
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
        client.get(All_ROOMS)
    }
    suspend fun freeRooms() = on<RoomsResponse> {
        client.get(All_ROOMS){
            parameter("free",true)
        }
    }

    fun connectToGame(id: String, sharedFlow: SharedFlow<Point>) = flow<Frame.Text> {
        client.webSocket(
            method = HttpMethod.Get,
            host = HOST,
            path = "/$id",
            request = { parameter("userId", Build.ID.toString()) }
        ) {
            launch {
                sharedFlow.collectLatest { frame ->
                    Log.d("SEND_DATA", frame.toString())
                    outgoing.send(Frame.Text(sendCtrlProtocol(frame)))
                }
            }
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    emit(frame)
                    Log.d("DQOFNQEFQEF", frame.readText())
//                    messageReceive(frame, ctrlProtocol)
                }
                else {
                    Log.d("AAA", frame.data.toString())
                }
            }
        }
    }
}