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