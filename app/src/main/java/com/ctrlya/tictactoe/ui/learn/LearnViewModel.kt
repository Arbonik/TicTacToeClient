package com.ctrlya.tictactoe.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.database.memory.MemoryInteractor

class LearnViewModel(
    memoryInteractor: MemoryInteractor
) : ViewModel() {

    val game = LearnService(
        BattlefieldSettings(
            11,
            11,
            5,
            false
        ),
        viewModelScope,
        memoryInteractor
    )

    // подключение игроков к игре и
    fun start() {
        game.start()
//        game.setPlayer(
//            player,
//            player1,
//            player
//        )
//        viewModelScope.launch {
//            player.connectToGame(game)
//        }
//        viewModelScope.launch {
//            player1.connectToGame(game)
//        }
    }
}