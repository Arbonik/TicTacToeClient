package com.ctrlya.tictactoe.ui.game

class GameModeItem(
    val title: String,
    val subtitle: String,
    val gameMode: () -> Unit = {}
)