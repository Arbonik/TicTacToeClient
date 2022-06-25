package com.ctrlya.tictactoe.database.memory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {

    @Query("SELECT * FROM movesTable WHERE botId = :playerId")
    suspend fun getMoves(playerId: Long): List<MovePosition>

    @Query("SELECT * FROM movesTable WHERE botId = :playerId")
    fun getMovesFlow(playerId: Long): Flow<List<MovePosition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj : List<MovePosition>)
}