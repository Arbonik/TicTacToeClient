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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetworkPlayer(
    override val mark: Mark,
) : Player {
    val outgoingChannel: MutableSharedFlow<String> = MutableSharedFlow()
    val turns: MutableSharedFlow<Point> = MutableSharedFlow()

    override suspend fun connectToGame(game: GameService) {
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