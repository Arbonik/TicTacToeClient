package com.ctrlya.tictactoe.ui.learn

import android.app.AlertDialog
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.databinding.FragmentLearnBinding
import com.ctrlya.tictactoe.network.GameResult
import com.ctrlya.tictactoe.network.GameStatus
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
                    GameEvent.CREATED -> {
                        binding.textMessages.text = "Начинаем?"
                    }
                    GameEvent.DRAW -> {
                        gameFinish(GameResult.Draw)
                    }
                    GameEvent.END -> {
                        gameFinish(GameResult.Win)
                    }
                    GameEvent.INIT -> {}
                    is GameEvent.Start -> {
                            binding.textMessages.text = "Начинаем?"
                    }
                    is GameEvent.Turn -> {
                        if (value.player == learnViewModel.game.teatcher) {
                            binding.textMessages.text = "Ваш ход, гроссмейстер?"
                        } else {
                            binding.textMessages.text = "Можно я похожу?"
                        }
                    }
                    is GameEvent.Win -> {
                        if (value.winner == learnViewModel.game.teatcher) {
                            gameFinish("Спасибо за урок!")
                        } else {
                            gameFinish("Ученик превзошел учителя!")
                        }
                    }
                    is GameEvent.Message -> {
                        binding.textMessages.text = value.message
                    }
                }
            }
        }
        return binding.root
    }

    private fun gameFinish(gameResult: GameResult) {
        val message = when (gameResult) {
            GameResult.Win -> "Позвравляем!"
            GameResult.Lose -> "Успехов в следующий раз!"
            GameResult.Draw -> "Ничья"
        }
        gameFinish(message)
    }

    private fun gameFinish(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(message)
            .setPositiveButton("Ok") { _, _ ->
                restartGame()
            }
            .show()
    }

    private fun restartGame() {
        findNavController().navigate(R.id.action_learnFragment_self)
    }
}