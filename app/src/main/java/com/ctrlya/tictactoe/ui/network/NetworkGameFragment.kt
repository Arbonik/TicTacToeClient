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
import com.ctrlya.tictactoe.core.player.NetworkPlayer
import com.ctrlya.tictactoe.core.player.RealPlayer
import com.ctrlya.tictactoe.databinding.FragmentLearnBinding
import com.ctrlya.tictactoe.databinding.FragmentNetworkGameBinding
import com.ctrlya.tictactoe.databinding.NetworkGameFragmentBinding
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkGameFragment : Fragment() {

    private val viewModel: ConnectGameViewModel by viewModel<ConnectGameViewModel>()
    private lateinit var binding: NetworkGameFragmentBinding
    val firstPlayer = RealPlayer(Mark.X, lifecycleScope)
    val game = GameService(BattlefieldSettings(3, 3, 3, false), lifecycleScope)
    val networkPlayer = NetworkPlayer(Mark.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NetworkGameFragmentBinding.inflate(inflater, container, false)
        binding.tictacktoeview
        game.setPlayer(firstPlayer, networkPlayer, firstPlayer)

        lifecycleScope.launchWhenCreated {
            binding.tictacktoeview.setField(game.battlefieldStateFlow)
        }
        lifecycleScope.launchWhenCreated {
            firstPlayer.connectToGame(game)
        }
        lifecycleScope.launchWhenCreated {
            firstPlayer.connectToTouchListener(binding.tictacktoeview)
        }
        lifecycleScope.launchWhenCreated {
            networkPlayer.connectToGame(game)
        }

        val id = arguments?.getString("id")
        if (id != null) {
            lifecycleScope.launchWhenCreated {
                viewModel.ws(id, networkPlayer.turns.asSharedFlow()){
                    game.playerTurn(networkPlayer, it)
                }
            }
        } else {
            Toast.makeText(requireContext(), "ID не передан", Toast.LENGTH_LONG).show()
        }
        return binding.root
    }
}

