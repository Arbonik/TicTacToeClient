package com.ctrlya.tictactoe.ui.network

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

class NetworkGameFragment : Fragment() {

    private val viewModel: NetworkGameViewModel by viewModel<NetworkGameViewModel>()

    val player = RealPlayer(Mark.O, lifecycleScope)
    val game = GameService(BattlefieldSettings(3,3,3,false), lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getString("id")?.let { itd ->
            val player1 = viewModel.connectToGame(Mark.O, itd)

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
                player1.connectToGame(game)
            }
            lifecycleScope.launchWhenCreated {
                player.connectToGame(game)
            }
        }
        return view //inflater.inflate(R.layout.network_game_fragment, container, false)
    }

}