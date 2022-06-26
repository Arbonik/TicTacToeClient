package com.ctrlya.tictactoe.core.player

import android.util.Log
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.data.toHash
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class SkilledBotPlayer(
    override val mark: Mark,
    val memoryInteractor: MemoryInteractor,
    val coroutineScope: CoroutineScope
) : Player {

    private val mutableSharedFlow: MutableSharedFlow<Point> = MutableSharedFlow()

    private var memory: LinkedHashMap<String, Point> = linkedMapOf()

    init {
        coroutineScope.launch {
            memory = memoryInteractor.memorySnupshot(LearnBot.HOBOT)
        }
    }

    override suspend fun connectToGame(game: GameService) {

    }

    suspend fun connectToTouchListener(ticTacToeView: TicTacToeView) {
        ticTacToeView.clicked.map { point ->
            var turns = mutableListOf<Point>()
            val field = ticTacToeView.getField()
            for (i in field.indices) {
                for (j in field[i].indices) {
                    if (field[i][j] == Mark.EMPTY) {
                        turns.add(Point(j, i))
                    }
                }
            }
            val tryPoint = memory[field.toHash()]
            var p: Point = tryPoint ?: turns[Random.nextInt(turns.size)]
            if (turns.contains(tryPoint).not()) {
                p = turns[Random.nextInt(turns.size)]
                Log.d("TURNER", "RANDOM")
            } else{
                Log.d("TURNER", "MEMORY")
            }
            p
        }.collectLatest { point ->
            mutableSharedFlow.emit(point)
        }
    }

    override suspend fun turn(): Flow<Point> = mutableSharedFlow.asSharedFlow()
}