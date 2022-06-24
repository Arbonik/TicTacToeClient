package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.Game
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameProgress
import com.ctrlya.tictactoe.core.game.GameService
import kotlinx.coroutines.flow.Flow

open class ConsolePlayer(
    playerId: String,
    override val mark: Mark
) : Player{
    suspend fun connectToGameStatus(gameService: GameService){
        gameService.gameStatusFlow.collect {
            when (it){
                GameProgress.CREATED -> { }
                GameProgress.DRAW -> {}
                GameProgress.END -> {}
                GameProgress.INIT -> {}
                is GameProgress.Start -> {
                    if (it.firstTurn == this){
                        gameService.playerTurn(this, turn(gameService.marks))
                    }
                }
                is GameProgress.Turn -> {
                    if (it.player != this){
                        gameService.playerTurn(this, turn(gameService.marks))
                    }
                }
                is GameProgress.Win -> {}
            }
        }
    }
    open fun turn(field: List<List<Mark>>): Point {
        println("Куда походить?")
        val position = Point(
            readLine()!!.toInt(),
            readLine()!!.toInt()
        )
        return position
    }

    override suspend fun connectToGame(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun turn(point: Point): Flow<Point> {
        TODO("Not yet implemented")
    }
}