package com.arbonik.tictactoebot.core

import android.graphics.Point
import com.arbonik.tictactoebot.core.utils.height
import com.arbonik.tictactoebot.core.utils.width

class TurnValidator {
    companion object {
        fun validate(point: Point, marks: Array<Array<Mark>>): TurnValidateStatus {
            val x = point.x
            val y = point.y
            return if (x in 0 until marks.width && y in 0 until marks.height) {
                return if (marks[y][x] == Mark.EMPTY)
                    TurnValidateStatus.SUCCESS
                else TurnValidateStatus.IS_NOT_EMPTY
            } else TurnValidateStatus.OUT_OF_RANGE
        }
    }
}