package com.ctrlya.tictactoe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctrlya.tictactoe.R
import org.koin.androidx.viewmodel.ext.android.viewModel

open class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModel<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.createRoom()
        return inflater.inflate(R.layout.game_fragment, container, false)
    }
}