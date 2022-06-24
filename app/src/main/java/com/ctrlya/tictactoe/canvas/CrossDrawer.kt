package com.ctrlya.tictactoe.canvas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class CrossDrawer : Drawn() {

    var state = AnimState.ANIMATING
    val CROSS_XY = 270f

    var drawn = 0

    var posX = 0
    var posY = 0

    val stopX: Float
        get() = minOf(animationCurrentTime.toFloat() / animationTime * CROSS_XY, CROSS_XY)

    override fun draw(canvas: Canvas) {

        animationTime = 10_00

        val p = Paint().apply {
            color = Color.BLUE
            strokeWidth = 15f
        }
        if (state == AnimState.ANIMATING) {
            if (animationCurrentTime > animationTime || drawn != 0) {
                drawn++
                if (drawn == 1) {
                    createTime = System.currentTimeMillis()
                }
                canvas.drawLine(
                    CROSS_XY * posX,
                    CROSS_XY * posY,
                    CROSS_XY * (posX + 1),
                    CROSS_XY * (posY + 1),
                    p
                )
                canvas.drawLine(
                    CROSS_XY * (posX + 1),
                    CROSS_XY * posY,
                    posX * CROSS_XY + (CROSS_XY - stopX),
                    stopX + CROSS_XY * posY,
                    p
                )
            } else if (drawn == 0) {
                canvas.drawLine(
                    CROSS_XY * posX,
                    CROSS_XY * posY,
                    stopX + CROSS_XY * posX,
                    stopX + CROSS_XY * posY,
                    p
                )
            }
        }
    }

    override fun setPosition(x: Int, y: Int) {
        posX = x
        posY = y
    }
}