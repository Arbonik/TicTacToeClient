package com.ctrlya.tictactoe.ui.game

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.BotPlayer
import com.ctrlya.tictactoe.core.player.RealPlayer
import com.ctrlya.tictactoe.core.player.SkilledBotPlayer
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BotGameFragment : Fragment() {
    val memoryInteractor: MemoryInteractor by inject()
    private val viewModel: BotGameViewModel by viewModel()
    val player = RealPlayer(Mark.O, lifecycleScope)
    val player1 = SkilledBotPlayer(Mark.X, memoryInteractor, lifecycleScope)
    val game = GameService(BattlefieldSettings(10, 10, 5, false), lifecycleScope)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel.createRoom()
        val view = TicTacToeView(
            requireContext(),
            null
        ) //inflater.inflate(R.layout.game_fragment, container, false)
        game.setPlayer(
            player,
            player1,
            player,
        )
        lifecycleScope.launch {
            val builder = AlertDialog.Builder(requireContext())
            game.gameStatusFlow.collect {
                when (it) {
                    is GameEvent.Win -> {
                        if (it.winner == player) {
                            builder.setMessage("Вы победили бота!")
                        } else {
                            builder.setMessage("Бот победил!")
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

        lifecycleScope.launchWhenCreated {
            view.setField(game.battlefieldStateFlow)
        }
        lifecycleScope.launchWhenCreated {
            player.connectToTouchListener(view)
        }
        lifecycleScope.launchWhenCreated {
            player1.connectToTouchListener(view)
        }
        return view
    }
}