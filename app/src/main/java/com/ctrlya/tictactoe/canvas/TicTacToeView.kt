package com.ctrlya.tictactoe.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

open class TicTacToeView(context: Context, attributeSet: AttributeSet?) :
    GraphView(context, attributeSet) {

    private val markDrawer = CrossDrawer()
    private var _field: Array<Array<Mark>> = arrayOf(arrayOf(Mark.EMPTY))
    protected var viewField: MutableList<MutableList<Drawn>> =
        mutableListOf(mutableListOf(EmptyDrawer()))

    open val CELL_WIDTH = 270f
    open val CELL_HEIGHT = 270f


    private fun collect() {
        viewField = mutableListOf()
        for (i in _field.indices) {
            viewField.add(mutableListOf())
            for (j in _field[i].indices) {
                viewField[i].add(markToDrawn(_field[i][j]))
            }
        }
    }

    private fun markToDrawn(mark: Mark) = when (mark) {
        Mark.EMPTY -> EmptyDrawer()
        Mark.X -> CrossDrawer()
        Mark.O -> ZeroDrawer()
    }


    private fun addToField(x: Int, y: Int, mark: Mark) {
        _field[x][y] = mark
        viewField[x][y] = markToDrawn(mark)
    }

    fun setField(field: Array<Array<Mark>>) {
        _field = field
        collect()
    }

    protected fun drawField(canvas: Canvas) {
//        Log.d("AAA", "width - ${canvas.width}")
//        Log.d("AAA", "height - ${canvas.height}")
        val p = Paint().apply {
            color = Color.BLACK
            strokeWidth = 10f
        }
        for (i in 0.._field.size) {
            canvas.drawLine(CELL_WIDTH * i, 0f, CELL_HEIGHT * i, _field[0].size * CELL_WIDTH, p)
        }
        for (i in 0.._field[0].size) {
            canvas.drawLine(0f, CELL_HEIGHT * i, _field.size * CELL_WIDTH, CELL_HEIGHT * i, p)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE)

        drawField(canvas)


        viewField.forEachIndexed { x, array ->
            array.forEachIndexed { y, it ->
                it.setPosition(x, y)
                it.draw(canvas)
            }
        }
        invalidate()
    }

    var clicked: (point: Point) -> Unit = {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("canvasXXX", canvasX.toString())
        Log.d("canvasXXX", canvasY.toString())
        val x = (event.x / scaleFactor - canvasX) / CELL_WIDTH
        val y = (event.y / scaleFactor - canvasY) / CELL_HEIGHT
        Log.d("canvasXXX", x.toString())
        Log.d("canvasXXX", y.toString())

        val point = Point(x.toInt(), y.toInt())

        clicked(point)

        if (point.x <= 3 && point.y <= 3 && dragging.not()) {
            addToField(point.x, point.y, Mark.X)
        }
        invalidate()

        return super.onTouchEvent(event)
    }
}