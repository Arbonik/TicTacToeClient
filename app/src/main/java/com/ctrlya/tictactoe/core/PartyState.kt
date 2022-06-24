package com.arbonik.tictactoebot.core

import android.graphics.Point
import com.arbonik.tictactoebot.core.player.Player

sealed class PartyState(val stateId: Int) {
    class PLAY(val prevTurn: Point) : PartyState(0)
    class RESULT(val winner: Player? = null) : PartyState(1)
    object DRAW : PartyState(2)
    object ON_START : PartyState(3)
}