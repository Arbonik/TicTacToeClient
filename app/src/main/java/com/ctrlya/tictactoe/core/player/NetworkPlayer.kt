package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.network.*
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json

class NetworkPlayer(
    override val mark: Mark,
    val id: String,
    val ktorClient: TicTacToeClient,
) : Player, CtrlProtocol {

    val outgoingChannel : BroadcastChannel<GameEvent> = BroadcastChannel(1)
    private val turns : MutableSharedFlow<Point> = MutableSharedFlow()

    override suspend fun connectToGame(game: GameService) {
        ktorClient.connectToGame(id, this)

        game.gameStatusFlow.collectLatest { game ->
            when (game){
                GameEvent.CREATED -> {}
                GameEvent.DRAW -> {

                }
                GameEvent.END -> {}
                GameEvent.INIT -> {}
                is GameEvent.Start -> {}
                is GameEvent.Turn -> {
                    turns.emit(game.point)
                }
                is GameEvent.Win -> {

                }
            }
        }
    }

    override suspend fun turn(): Flow<Point> = turns
    override suspend fun point(point: Point) {
        
    }

    override suspend fun chat(message: String) {

    }

    override suspend fun event(event: GameStatus) {

    }
}