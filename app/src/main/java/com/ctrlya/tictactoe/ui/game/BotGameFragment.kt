package com.ctrlya.tictactoe.ui.game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.BotPlayer
import com.ctrlya.tictactoe.core.player.RealPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class BotGameFragment : Fragment() {

    private val viewModel: BotGameViewModel by viewModel<BotGameViewModel>()
    val player = RealPlayer(Mark.X, lifecycleScope)
    val player1 = BotPlayer(Mark.O, lifecycleScope)
    val game = GameService(BattlefieldSettings(10, 10, 15, false), lifecycleScope)
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