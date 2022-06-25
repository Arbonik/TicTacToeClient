package com.ctrlya.tictactoe.core.party.transformers

import com.ctrlya.tictactoe.core.data.Point

class PointTransformer {
    companion object {
        fun rotate(point: Point, center: Pair<Double, Double>): Point {
            val dx = point.x - center.first
            val dy = point.y - center.second
            val newY = dx
            val newX = -dy
            return Point(
                (newX + center.first).toInt(),
                (newY + center.second).toInt()
            )
        }
    }
}