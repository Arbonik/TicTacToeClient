package com.ctrlya.tictactoe

import com.ctrlya.tictactoe.core.player.Player

interface Game {
    suspend fun connect(player: Player)
}