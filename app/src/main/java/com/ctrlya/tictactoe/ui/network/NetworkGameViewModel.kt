package com.ctrlya.tictactoe.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.player.NetworkPlayer
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.TicTacToeClient

class NetworkGameViewModel(
    val networkGameInteractor: NetworkGameInteractor,
    val ticTacToeClient: TicTacToeClient
) : ViewModel() {
    fun connectToGame(mark: Mark, id : String) : NetworkPlayer {
        return NetworkPlayer(mark, id, ticTacToeClient)
    }

}