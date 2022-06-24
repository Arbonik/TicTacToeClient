package com.arbonik.tictactoebot.core.utils

import android.graphics.Point
import com.arbonik.tictactoebot.core.Mark
import com.arbonik.tictactoebot.core.Party
import com.arbonik.tictactoebot.core.TurnValidateStatus
import com.arbonik.tictactoebot.core.TurnValidator

abstract class PartyController {
    abstract val party: Party

    protected open fun turn(point: Point, mark: Mark): Boolean {
        return turn(point.x, point.y, mark)
    }

    protected open fun turn(x: Int, y: Int, mark: Mark): Boolean {
        return if (validateTurn(Point(x, y)) == TurnValidateStatus.SUCCESS) {
            party.turn(
                x,
                y,
                mark
            )
            true
        } else false
    }

    protected fun restart() = party.restart()

    open fun validateTurn(point: Point): TurnValidateStatus =
        TurnValidator.validate(point, party.field.value)
}