package com.arbonik.tictactoebot.core.player

import android.graphics.Point
import com.arbonik.tictactoebot.core.Mark
import com.arbonik.tictactoebot.core.utils.BattlePartyController
import com.arbonik.tictactoebot.core.utils.PartyController

abstract class Player {
    abstract val name : String
    abstract val mark : Mark
    abstract fun turn(field: Array<Array<Mark>>): Point
    abstract suspend fun attachToGame(partyController: PartyController)
}