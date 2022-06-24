package com.arbonik.tictactoebot.core.utils.transformers

import android.graphics.Point

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