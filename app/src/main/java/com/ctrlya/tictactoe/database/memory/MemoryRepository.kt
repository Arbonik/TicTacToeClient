package com.ctrlya.tictactoe.database.memory

import com.ctrlya.tictactoe.core.data.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.transform

class MemoryRepository(
    var memoryDao: MemoryDao
) {
    suspend fun save(playerId: Long, data: List<Pair<String, Point>>) {
        val movePositionsList = List(data.size) {
            MovePosition(
                playerId,
                data[it].first,
                data[it].second.x,
                data[it].second.y
            )
        }
        memoryDao.insert(movePositionsList)
    }

    suspend fun load(playerId: Long): LinkedHashMap<String, Point> {
        val moves = memoryDao.getMoves(playerId)
        return LinkedHashMap(moves.map {
            it.position to Point(it.moveX, it.moveY)
        }.toMap())
    }

    fun memoryFlow(playerId: Long): Flow<LinkedHashMap<String, Point>> {
        return memoryDao.getMovesFlow(playerId)
            .conflate()
            .transform { value ->
                LinkedHashMap(
                    value.map {
                        it.position to Point(it.moveX, it.moveY)
                    }.toMap()
                )
            }
    }
}