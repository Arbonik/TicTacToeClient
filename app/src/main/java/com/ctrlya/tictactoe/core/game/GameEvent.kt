package com.ctrlya.tictactoe.core.game

import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.player.Player

sealed class GameEvent {
    /*When game created*/
    object CREATED : GameEvent()
    data class Message(val message : String) : GameEvent()
    /*When players connected to game*/
    object INIT : GameEvent()
    /*When player must do first turn*/
    class Start(val firstTurn : Player) : GameEvent()
    /*When player do turn*/
    class Turn(val player: Player, val point : Point) : GameEvent()
    object DRAW : GameEvent()
    class Win(val winner : Player) : GameEvent()
    object END : GameEvent()
}
