package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.Game
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RandomBot(playerId: String, mark: Mark) : ConsolePlayer(playerId, mark) {

    override fun turn(field: List<List<Mark>>): Point {
        val sequence = sequence {
            for (y in field.indices) {
                for (x in field[y].indices)
                    if (field[y][x] == Mark.EMPTY)
                        yield(Point(x, y))
            }
        }
        val turns = sequence.toList()
        if (turns.isNotEmpty())
            return turns.random()
        else return Point(0, 0)
    }

    override suspend fun turn(point: Point): Flow<Point> {
        return flow {  }
    }

    override suspend fun connectToGame(game: Game) {

    }
}