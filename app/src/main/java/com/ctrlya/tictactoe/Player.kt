package com.ctrlya.tictactoe

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Player {
    suspend fun connectToGame(game: Game)

    suspend fun turn(point : Point) : Flow<Point>

}

class RandomPlayer(
    val area : Point
) : Player{
    override suspend fun connectToGame(game: Game) {

    }

    override suspend fun turn(point: Point): Flow<Point> = flow {
        repeat(100) {
            emit(Point(area.x.rangeTo(0).random(), area.y.rangeTo(0).random()))
        }
    }

}
