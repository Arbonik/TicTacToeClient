package com.ctrlya.tictactoe.core.player

import android.util.Log
import com.ctrlya.tictactoe.core.data.*
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.party.transformers.FieldTransformer
import com.ctrlya.tictactoe.core.party.transformers.PointTransformer
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

// делает свои ходы наугад, если не знает позиции
class LearnBot(
    override val mark: Mark,
    val memoryInteractor: MemoryInteractor,
    val coroutineScope: CoroutineScope,
) : Player {
    companion object {
        const val HOBOT = 10_000_000L
    }

    // подписка на события игры
    override suspend fun connectToGame(game: GameService) {
    }

    override suspend fun turn(): Flow<Point> = flow {
        emit(Point())
    }

    private var fastMemory: LinkedHashMap<String, Point> = linkedMapOf()

    suspend fun fastMemoryUpdate() {
        fastMemory = memoryInteractor.memorySnupshot(HOBOT)
    }

    fun isFamiliarPosition(fieldHash: String) =
        fastMemory.contains(fieldHash)

    // ищет ход в памяти, если нет, ходит наугад
    fun turn(field: Array<Array<Mark>>): Point {
        var turns = mutableListOf<Point>()
        for (i in field.indices) {
            for (j in field[i].indices) {
                if (field[i][j] == Mark.EMPTY) {
                    turns.add(Point(j, i))
                }
            }
        }
        val tryPoint = fastMemory[field.toHash()]
        var p: Point = tryPoint ?: turns[Random.nextInt(turns.size)]
        if (turns.contains(tryPoint).not())
            p = turns[Random.nextInt(turns.size)]
        return p
    }

    // сохранение одной позиции со всех сторон
    suspend fun learn(point: Point, position: Array<Array<Mark>>) {
        val listToSave = mutableListOf<Pair<String, Point>>()
        listToSave.add(position.toHash() to point)

        var newPoint = PointTransformer.rotate(
            point,
            (position.width - 1).toDouble() / 2.0 to
                    (position.height - 1).toDouble() / 2.0
        )
        for (i in 0..2) {
            listToSave.add(FieldTransformer.rotate(position).toHash() to newPoint)
            newPoint = PointTransformer.rotate(
                newPoint,
                (position.width - 1).toDouble() / 2.0 to
                        (position.height - 1).toDouble() / 2.0
            )
        }
        memoryInteractor.save(HOBOT, listToSave)
        fastMemoryUpdate()
    }
}