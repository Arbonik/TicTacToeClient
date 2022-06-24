package com.arbonik.tictactoebot.core.player

import android.graphics.Point
import com.arbonik.tictactoebot.core.Mark
import com.arbonik.tictactoebot.core.utils.BattlePartyController
import com.arbonik.tictactoebot.core.utils.PartyController
import kotlinx.coroutines.flow.collectLatest

open class RandomPlayer(
    override val name: String,
    override val mark: Mark
) : Player() {

    override suspend fun attachToGame(partyController: PartyController) {
        (partyController as BattlePartyController).currentPlayer.collectLatest {
            collectToChangeCurrentPlayer(it, partyController)
        }
    }

    open fun collectToChangeCurrentPlayer(
        it: Player?,
        partyController: BattlePartyController
    ) {
        if (it == this) {
            partyController.playerTurn(
                turn(partyController.party.field.value),
                this
            )
        }
    }

    override fun turn(field: Array<Array<Mark>>): Point {
        val sequence = sequence {
            for (y in field.indices) {
                for (x in field[y].indices)
                    if (field[y][x] == Mark.EMPTY)
                        yield(Point(x, y))
            }
        }
        val turns = sequence.toList()

        if (turns.isNotEmpty())
            return turns.random()
        else return Point()
    }
}