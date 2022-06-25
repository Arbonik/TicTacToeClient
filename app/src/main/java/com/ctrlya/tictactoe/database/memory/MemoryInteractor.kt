package com.ctrlya.tictactoe.database.memory

import com.ctrlya.tictactoe.core.data.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MemoryInteractor constructor(
    val memoryRepository: MemoryRepository
)  {
    suspend fun save(playerId: Long, data: List<Pair<String, Point>>) {
        withContext(Dispatchers.IO) {
            memoryRepository.save(playerId, data)
        }
    }

    suspend fun memorySnupshot(playerId: Long): LinkedHashMap<String, Point> {
        return withContext(Dispatchers.IO) { memoryRepository.load(playerId) }
    }

    fun memoryFlow(playerId: Long) = memoryRepository.memoryFlow(playerId)
        .flowOn(Dispatchers.Main)
}
