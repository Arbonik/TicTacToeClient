package com.ctrlya.tictactoe.canvas

import android.graphics.Canvas

abstract class Drawn {

    var createTime: Long = System.currentTimeMillis()
    var animationTime: Long = 15_00

    val animationCurrentTime: Long
        get() = System.currentTimeMillis() - createTime

    abstract fun draw(canvas: Canvas)

    abstract fun setPosition(x: Int, y: Int)
}