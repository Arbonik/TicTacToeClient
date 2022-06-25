package com.ctrlya.tictactoe.core.player

import android.util.Log
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.GameService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class BotPlayer(override val mark: Mark, val botScope: CoroutineScope) : Player {

    private val mutableSharedFlow: MutableSharedFlow<Point> = MutableSharedFlow()

    suspend fun connectToTouchListener(ticTacToeView: TicTacToeView) {
        ticTacToeView.clicked.map { point ->
            var turns = mutableListOf<Point>()
            val field = ticTacToeView.getField()
            for (i in field.indices){
                for (j in field[i].indices){
                    if (field[i][j] == Mark.EMPTY){
                        turns.add(Point(j, i))
                    }
                }
            }
            val p = turns[Random.nextInt(turns.size)]
            Log.d("POINT_BOT", p.toString())
            p
        }.collectLatest { point ->
            mutableSharedFlow.emit(point)
        }
    }

    override suspend fun connectToGame(game: GameService) {

    }

    override suspend fun turn(): Flow<Point> = mutableSharedFlow.asSharedFlow()

}