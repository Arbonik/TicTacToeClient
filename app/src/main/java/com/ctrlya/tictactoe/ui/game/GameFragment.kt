package com.ctrlya.tictactoe.ui.game

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.RealPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModel<GameViewModel>()
    val player = RealPlayer(Mark.X, lifecycleScope)
    val player1 = RealPlayer(Mark.O, lifecycleScope)
    val game = GameService(BattlefieldSettings(10, 10, 5, false), lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel.createRoom()

        val ticTacToeView = TicTacToeView(
            requireContext(),
            null
        )

        viewModel.viewModelScope.launch {
            val builder = AlertDialog.Builder(requireContext())
            game.gameStatusFlow.collect {
                when (it) {
                    is GameEvent.Win -> {
                        if (it.winner == player) {
                            builder.setMessage("Победили крестики!")
                        } else {
                            builder.setMessage("Победили нолики!")
                        }
                        builder.create()
                        builder.show()
                    }
                    is GameEvent.DRAW -> {
                        builder.setTitle("Ничья!")
                        builder.create()
                        builder.show()
                    }
                }
            }
        }

         //inflater.inflate(R.layout.game_fragment, container, false)
        game.setPlayer(
            player,
            player1,
            player,
        )
        lifecycleScope.launchWhenCreated {
            ticTacToeView.setField(game.battlefieldStateFlow)
        }
        lifecycleScope.launchWhenCreated {
            player.connectToTouchListener(ticTacToeView)
        }
        lifecycleScope.launchWhenCreated {
            player1.connectToTouchListener(ticTacToeView)
        }
        return ticTacToeView
    }
}