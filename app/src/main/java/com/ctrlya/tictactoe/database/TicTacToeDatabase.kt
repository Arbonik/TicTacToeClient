package com.ctrlya.tictactoe.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ctrlya.tictactoe.database.memory.MovePosition
import com.ctrlya.tictactoe.database.memory.MemoryDao


@Database(
    entities = [
        MovePosition::class
    ],
    version = 1, exportSchema = false
)
abstract class TicTacToeDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
}