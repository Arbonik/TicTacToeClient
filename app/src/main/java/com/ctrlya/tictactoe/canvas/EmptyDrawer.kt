package com.ctrlya.tictactoe.canvas

import android.graphics.Canvas

class EmptyDrawer : Drawn() {

    var state = AnimState.ANIMATING

    override fun draw(canvas: Canvas) {

    }

    override fun setPosition(x: Int, y: Int) {

    }
}