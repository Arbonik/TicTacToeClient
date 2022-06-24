package com.ctrlya.tictactoe

interface Game {
    suspend fun connect(player: Player)
}