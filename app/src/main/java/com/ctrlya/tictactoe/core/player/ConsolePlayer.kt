package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameProgress
import com.ctrlya.tictactoe.core.game.GameService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

open class ConsolePlayer(
    playerId: String,
    override val mark: Mark
) : Player {
    suspend fun connectToGameStatus(gameService: GameService) {
        gameService.gameStatusFlow.collect {
            when (it) {
                GameProgress.CREATED -> {}
                GameProgress.DRAW -> {}
                GameProgress.END -> {}
                GameProgress.INIT -> {}
                is GameProgress.Start -> {
                    if (it.firstTurn == this) {
//                        gameService.playerTurn(this)
                    }
                }
                is GameProgress.Turn -> {
                    if (it.player != this) {
//                        gameService.playerTurn(this)
                    }
                }
                is GameProgress.Win -> {}
            }
        }
    }

    override suspend fun connectToGame(game: GameService) {
        game.battlefieldStateFlow.collectLatest {
            it.forEach { list ->
                println(list)
                println()
            }
        }
    }

    override suspend fun turn(): Flow<Point> = flow {
        while (true) {
            println("Куда походить?")
            val position = Point(
                readLine()!!.toInt(),
                readLine()!!.toInt()
            )
            emit(position)
        }
    }
}