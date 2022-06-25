package com.ctrlya.tictactoe.core.party

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point


class TurnValidator {
    companion object {
        fun validate(point: Point, marks: List<List<Mark>>): TurnStatus {
            val x = point.x
            val y = point.y
            return if (y in marks.indices && x in marks.first().indices) {
                return if (marks[y][x] == Mark.EMPTY)
                    TurnStatus.SUCCESS
                else TurnStatus.IS_NOT_EMPTY
            } else TurnStatus.OUT_OF_RANGE
        }
    }
}