package com.ctrlya.tictactoe.database.memory

import androidx.room.Entity
import androidx.room.PrimaryKey

//used only inside database
@Entity(tableName = "movesTable")
data class MovePosition(
    val botId : Long,
    val position : String,
    val moveX : Int,
    val moveY : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
