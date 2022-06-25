package com.ctrlya.tictactoe.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.LearnBot
import com.ctrlya.tictactoe.core.player.RealPlayer
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import kotlinx.coroutines.launch

class LearnViewModel(
    memoryInteractor: MemoryInteractor
) : ViewModel() {

    val player = RealPlayer(Mark.X, viewModelScope)

    val player1 = LearnBot(
        Mark.O,
        memoryInteractor,
        viewModelScope
    )
    val game = GameService(
        BattlefieldSettings(
            3,
            3,
            3,
            false
        ),
        viewModelScope
    )
    // подключение игроков к игре и
    fun start() {
        game.setPlayer(
            player,
            player1,
            player
        )
        viewModelScope.launch {
            player.connectToGame(game)
        }
        viewModelScope.launch {
            player1.connectToGame(game)
        }
    }
}