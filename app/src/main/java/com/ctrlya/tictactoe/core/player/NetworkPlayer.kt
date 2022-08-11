package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.network.sendCtrlProtocol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

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