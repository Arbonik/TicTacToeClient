package com.ctrlya.tictactoe.ui.network

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.RealPlayer
import com.ctrlya.tictactoe.databinding.FragmentLearnBinding
import com.ctrlya.tictactoe.databinding.FragmentNetworkGameBinding
import com.ctrlya.tictactoe.databinding.NetworkGameFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkGameFragment : Fragment() {

    private val viewModel: NetworkGameViewModel by viewModel<NetworkGameViewModel>()
    private lateinit var binding: NetworkGameFragmentBinding
    val firstPlayer = RealPlayer(Mark.O, lifecycleScope)
    val game = GameService(BattlefieldSettings(3, 3, 3, false), lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NetworkGameFragmentBinding.inflate(inflater, container, false)
        val view = binding.tictacktoeview

        lifecycleScope.launchWhenCreated {
            view.setField(game.battlefieldStateFlow)
        }
        lifecycleScope.launchWhenCreated {
            firstPlayer.connectToGame(game)
        }
        lifecycleScope.launchWhenCreated {
            val id = arguments?.getString("id")
            if (id != null) {
                val networkPlayer = viewModel.connectToGame(Mark.X, id)
                lifecycleScope.launch {
                    networkPlayer.connectToGame(game)
                    game.setPlayer(firstPlayer, networkPlayer, firstPlayer)
                }
            } else {
                Toast.makeText(requireContext(), "ID не передан", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root //inflater.inflate(R.layout.network_game_fragment, container, false)
    }
}