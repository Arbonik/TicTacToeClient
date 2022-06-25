package com.ctrlya.tictactoe.canvas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


class ZeroDrawer : Drawn() {
    var state = AnimState.ANIMATING

    val END = 360f

    var posX = 0
    var posY = 0

    val diam = 270f

    val angle: Float
        get() = minOf(animationCurrentTime.toFloat() / animationTime * END, END)

    override fun draw(canvas: Canvas) {

        animationTime = 12_00

        val p = Paint().apply {
            color = Color.BLUE
            strokeWidth = 15f
            style = Paint.Style.STROKE
        }
//        if (state == AnimState.ANIMATING) {
//            if (animationCurrentTime > animationTime) {
//                state = AnimState.IDLE
//            } else {
//                canvas.drawArc(250f, 0f, 500f, 250f, 0f, angle, false, p)
//            }
//        }
        if (state == AnimState.ANIMATING) {
            if (animationCurrentTime > animationTime) {
                state = AnimState.IDLE
            } else {
                canvas.drawArc(
                    diam * posX + 18f,
                    diam * posY + 18f,
                    diam * (posX + 1) - 18f,
                    diam * (posY + 1) - 18f,
                    0f,
                    angle,
                    false,
                    p
                )
            }
        } else {
            canvas.drawArc(
                diam * posX + 18f,
                diam * posY + 18f,
                diam * (posX + 1) - 18f,
                diam * (posY + 1) - 18f,
                0f,
                360f,
                false,
                p
            )
        }
    }

    override fun setPosition(x: Int, y: Int) {
        posX = x
        posY = y
    }
}