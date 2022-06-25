package com.ctrlya.tictactoe.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.RealPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

open class GameFragment : Fragment()
{

    private val viewModel: GameViewModel by viewModel<GameViewModel>()

    val player = RealPlayer(Mark.X, lifecycleScope)
    val player1 = RealPlayer(Mark.O, lifecycleScope)

    val game = GameService(BattlefieldSettings(3,3,3,false), lifecycleScope)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        viewModel.createRoom()
        val view = TicTacToeView(requireContext(), null) //inflater.inflate(R.layout.game_fragment, container, false)
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