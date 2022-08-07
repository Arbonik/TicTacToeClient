package com.ctrlya.tictactoe.ui.network

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameEvent
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
    val game = GameService(BattlefieldSettings(11, 11, 5, false), lifecycleScope)
    val networkPlayer = NetworkPlayer(Mark.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NetworkGameFragmentBinding.inflate(inflater, container, false)
        binding.tictacktoeview

        game.setPlayer(firstPlayer, networkPlayer, networkPlayer)

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
//        if (game.currentPlayer != firstPlayer) {
//            val realFirstPlayer = RealPlayer(Mark.X, lifecycleScope)
//            game.setPlayer(realFirstPlayer, networkPlayer, realFirstPlayer)
//        }

        val id = arguments?.getString("id")
        if (id != null) {
            lifecycleScope.launchWhenCreated {
                viewModel.ws(id, networkPlayer.turns.asSharedFlow(), callback = {
                    game.playerTurn(networkPlayer, it)
                }){
                    game.setFirstPlayer(it)
                    Log.d("CURRENT_MARK", it.toString())
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
                game.gameStatusFlow.collect {
                    when (it) {
                        is GameEvent.Win -> {
                            if (it.winner == firstPlayer) {
                                Toast.makeText(requireContext(), "Победа", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Не победа", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is GameEvent.DRAW -> {
                            Toast.makeText(requireContext(), "Ничья", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "ID не передан", Toast.LENGTH_LONG).show()
        }
        return binding.root
    }
}

