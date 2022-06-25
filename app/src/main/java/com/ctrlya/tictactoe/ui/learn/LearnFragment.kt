package com.ctrlya.tictactoe.ui.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.databinding.FragmentLearnBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class LearnFragment : Fragment() {

    private val learnViewModel: LearnViewModel by viewModel()
    private lateinit var binding: FragmentLearnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        learnViewModel.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenCreated {
            binding.tictacktoeview.setField(learnViewModel.game.battlefieldStateFlow)
        }
        // подключение человека ко view
        lifecycleScope.launchWhenCreated {
            learnViewModel.game.teatcher.connectToTouchListener(binding.tictacktoeview)
        }
        // подключение фрагмента к состоянию игры
        lifecycleScope.launchWhenCreated {
            learnViewModel.game.gameStatusFlow.collectLatest { value: GameEvent ->
                when (value) {
                    GameEvent.CREATED -> {}
                    GameEvent.DRAW -> {}
                    GameEvent.END -> {
                    }
                    GameEvent.INIT -> {}
                    is GameEvent.Start -> {}
                    is GameEvent.Turn -> {}
                    is GameEvent.Win -> {
//                        AlertDialog.Builder(requireContext())
//                            .setTitle("Поздравляем! Вы выиграли")
//                            .setPositiveButton("Ok") { _, _ ->
//                                restartGame()
//                            }
//                            .show()
                    }
                    is GameEvent.Message -> {
                        binding.textMessages.text = value.message
                    }
                }
            }
        }
        return binding.root
    }

    private fun restartGame() {
        findNavController().navigate(R.id.action_learnFragment_self)
    }
}