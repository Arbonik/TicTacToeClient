package com.ctrlya.tictactoe.ui.game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctrlya.tictactoe.R

class BotGameFragment : Fragment() {

    companion object {
        fun newInstance() = BotGameFragment()
    }

    private lateinit var viewModel: BotGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bot_game, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BotGameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}