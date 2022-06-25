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

    val game = GameService(BattlefieldSettings(3, 3, 3, false), lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = NetworkTicTacToeView(
            requireContext(),
            null
        )

        lifecycleScope.launchWhenCreated {
            view.setField(game.battlefieldStateFlow)
        }
        lifecycleScope.launchWhenCreated {
            view.connectToGame("a5b662bb-c39f-489e-8b2d-d83ea8245de3", lifecycleScope)
        }
        return view //inflater.inflate(R.layout.network_game_fragment, container, false)
    }

}