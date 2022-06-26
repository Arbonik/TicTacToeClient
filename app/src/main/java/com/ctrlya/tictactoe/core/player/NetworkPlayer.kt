package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.network.CtrlProtocol
import com.ctrlya.tictactoe.network.GameStatus
import com.ctrlya.tictactoe.network.TicTacToeClient
import com.ctrlya.tictactoe.network.sendCtrlProtocol
import io.ktor.websocket.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

class NetworkPlayer(
    override val mark: Mark,
    val id: String,
    val ktorClient: TicTacToeClient,
) : Player {

    private val socketListener = object : CtrlProtocol {
        override suspend fun point(point: Point) {
            turns.emit(point)
        }

        override suspend fun chat(message: String) {

        }

        override suspend fun event(event: GameStatus) {

        }
    }

    val outgoingChannel: MutableSharedFlow<String> = MutableSharedFlow()
    private val turns: MutableSharedFlow<Point> = MutableSharedFlow()

    override suspend fun connectToGame(game: GameService) {
        ktorClient.connectToGame(id, socketListener, outgoingChannel)

        game.gameStatusFlow.collectLatest { value ->
            when (value) {
                GameEvent.CREATED -> {}
                GameEvent.DRAW -> {

                }
                GameEvent.END -> {}
                GameEvent.INIT -> {}
                is GameEvent.Start -> {}
                is GameEvent.Turn -> {
                    if (value.player == this){
                        outgoingChannel.emit(sendCtrlProtocol(value.point))
                    }
                    turns.emit(value.point)
                }
                is GameEvent.Win -> {

                }
                else -> {}
            }
        }
    }

    override suspend fun turn(): Flow<Point> = turns

}