package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class RealPlayer(override val mark: Mark, val playerScope : CoroutineScope) : Player {

    private val mutableSharedFlow: MutableSharedFlow<Point> = MutableSharedFlow()

    suspend fun connectToTouchListener(ticTacToeView: TicTacToeView) {
        ticTacToeView.clicked.collectLatest { point ->
            mutableSharedFlow.emit(Point(point.x, point.y))
        }
    }

    override suspend fun connectToGame(game: GameService) {

    }

    override suspend fun turn(): SharedFlow<Point> = mutableSharedFlow.asSharedFlow()
}