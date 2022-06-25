package com.ctrlya.tictactoe.ui.network

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.network.CtrlProtocol
import com.ctrlya.tictactoe.network.GameStatus
import com.ctrlya.tictactoe.network.messageReceive
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class NetworkTicTacToeView(context: Context, attributeSet: AttributeSet?) :
    TicTacToeView(context, attributeSet) {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(WebSockets)
    }

    fun connectToGame(id:String, coroutineScope: CoroutineScope){
        val url = URLBuilder(protocol = URLProtocol.WS, host = "89.223.123.239", port = 8888, pathSegments = listOf(id)).buildString()
        coroutineScope.launch {
            client.webSocket(url){
                for (frame in incoming){
                    if (frame is Frame.Text) {
                        messageReceive(frame, object : CtrlProtocol {
                            override suspend fun point(point: Point) {
                                clicked.emit(android.graphics.Point(point.x, point.y))
                            }

                            override suspend fun chat(message: String) {
                            }

                            override suspend fun event(event: GameStatus) {
                            }
                        })
                    }
                }
            }
        }
    }
}