package com.arbonik.tictactoebot.ui.game.base

import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.arbonik.tictactoebot.R
import com.arbonik.tictactoebot.component.TicTacToeView
import com.arbonik.tictactoebot.core.utils.PartyController
import com.arbonik.tictactoebot.database.game.Game
import com.arbonik.tictactoebot.databinding.GameFragmentBinding
import kotlinx.coroutines.flow.collectLatest

abstract class GameFragment<TM : GameViewModel<T>, T : PartyController> : Fragment() {

    protected abstract val gameViewModel: TM
    abstract val game : Game?

    private lateinit var binding: GameFragmentBinding
    private lateinit var battleground: TicTacToeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        game?.let { gameViewModel.loadGame(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameFragmentBinding.inflate(inflater)
//        battleground = binding.battleground
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCurrentPlayerObserver()
        initBattlegroundObserver()
        initBattlegroundListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_game, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.restart_game -> {
                gameViewModel.restartGame()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initCurrentPlayerObserver() {
        lifecycleScope.launchWhenStarted {
            gameViewModel.gameMessages.collectLatest {
                binding.gameStatus.text = it
            }
        }
    }

    private fun initBattlegroundObserver() {
        lifecycleScope.launchWhenStarted {
            gameViewModel.field.collectLatest {
//                battleground.updateField(it)
            }
        }
    }

    private fun initBattlegroundListener() {
        battleground.setOnTouchListener { v, event ->
            val turn = Point(
                event.x.toInt() / battleground.dx,
                event.y.toInt() / battleground.dy
            )
            if (event.action == MotionEvent.ACTION_DOWN)
                gameViewModel.onClick(turn)
            v.performClick()
        }
    }
}
