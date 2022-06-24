package com.ctrlya.tictactoe.core.game

import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.player.Player

sealed class GameProgress {
    /*When game created*/
    object CREATED : GameProgress()
    /*When players connected to game*/
    object INIT : GameProgress()
    /*When player must do first turn*/
    class Start(val firstTurn : Player) : GameProgress()
    /*When player do turn*/
    class Turn(val player: Player, val point : Point) : GameProgress()
    object DRAW : GameProgress()
    class Win(val winner : Player) : GameProgress()
    object END : GameProgress()
}
