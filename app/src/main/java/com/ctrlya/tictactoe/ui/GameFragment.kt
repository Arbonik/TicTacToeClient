package com.ctrlya.tictactoe.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.TicTacToePlayerView
import com.ctrlya.tictactoe.canvas.EmptyDrawer
import com.ctrlya.tictactoe.canvas.Mark
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModel<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return TicTacToePlayerView(requireContext(), null, lifecycleScope).apply {
            lifecycleScope.launch {
                turn().collect {
                    Log.d("TURN", it.toString())
                }
            }
            setField(
                arrayOf(
                    arrayOf(Mark.EMPTY, Mark.EMPTY, Mark.EMPTY, Mark.EMPTY),
                    arrayOf(Mark.EMPTY, Mark.EMPTY, Mark.EMPTY, Mark.EMPTY),
                    arrayOf(Mark.EMPTY, Mark.O, Mark.EMPTY, Mark.EMPTY),
                    arrayOf(Mark.EMPTY, Mark.X, Mark.EMPTY, Mark.EMPTY)
                ),
            )
        }
    }
}