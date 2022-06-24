package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameService
import kotlinx.coroutines.flow.*

interface Player {
    suspend fun connectToGame(game: GameService)

    suspend fun turn(): Flow<Point>

    val mark : Mark
}

class RandomPlayer(
    val area: Point,
    override val mark: Mark
) : Player {
    override suspend fun connectToGame(game: GameService) {

    }

    override suspend fun turn(): Flow<Point> = flow {
        repeat(100) {
            emit(Point(area.x.rangeTo(0).random(), area.y.rangeTo(0).random()))
        }
    }

}