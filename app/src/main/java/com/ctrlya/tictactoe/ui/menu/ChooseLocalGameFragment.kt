package com.ctrlya.tictactoe.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctrlya.tictactoe.databinding.FragmentChooseNetworkGameBinding
import com.ctrlya.tictactoe.ui.game.GameMode
import com.ctrlya.tictactoe.ui.game.GameModeItem


class ChooseLocalGameFragment : Fragment()
{
    private lateinit var binding: FragmentChooseNetworkGameBinding
    private var adapter = GameModeAdapter(
        { gameMode: GameMode -> gameModeItemClickListener(gameMode) },
        arrayOf(
            GameModeItem(GameMode.LearningBot, "ОБУЧЕНИЕ\nXOБОТА", "Научи своего XOбота\nиграть лучше всех"),
            GameModeItem(GameMode.BvB, "БИТВА \nс ХОБОТОМ", "Не на жизнь,\nа вусмерть"),
        ),
        false
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentChooseNetworkGameBinding.inflate(inflater)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.gameModeList.adapter = adapter
        binding.gameModeList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        return binding.root
    }

    fun gameModeItemClickListener(gameMode: GameMode)
    {
        when (gameMode)
        {
            GameMode.PvB -> {}
            GameMode.BvB -> {}
            GameMode.LearningBot -> {}
            else -> {}
        }
    }

}