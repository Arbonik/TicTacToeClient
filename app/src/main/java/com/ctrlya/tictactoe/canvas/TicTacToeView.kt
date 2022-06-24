package com.ctrlya.tictactoe.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.ctrlya.tictactoe.core.data.Mark
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class TicTacToeView(context: Context, attributeSet: AttributeSet?) :
    GraphView(context, attributeSet) {

    private var _field: List<List<Mark>> = listOf(listOf(Mark.EMPTY))
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
        Log.d("FIELD_UPDATE", viewField.toString())
    }

    private fun markToDrawn(mark: Mark) = when (mark) {
        Mark.EMPTY -> EmptyDrawer()
        Mark.X -> CrossDrawer()
        Mark.O -> ZeroDrawer()
    }

    suspend fun setField(fieldState: StateFlow<List<List<Mark>>>) {
        fieldState.collectLatest { field ->
            _field = field
            Log.d("FIELD_UPDATE", "FIELD_UPDATE")
            collect()
            invalidate()
        }
    }

    private val p = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    protected fun drawField(canvas: Canvas) {
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
        invalidate()

        viewField.forEachIndexed { x, array ->
            array.forEachIndexed { y, it ->
                it.setPosition(x, y)
                it.draw(canvas)
            }
        }
    }

    val clicked: MutableSharedFlow<Point> = MutableSharedFlow()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = (event.x / scaleFactor - canvasX) / CELL_WIDTH
        val y = (event.y / scaleFactor - canvasY) / CELL_HEIGHT
        val point = Point(x.toInt(), y.toInt())

        if (event.action == MotionEvent.ACTION_UP) {
            MainScope().launch {
                clicked.emit(point)
            }
        }
        return super.onTouchEvent(event)
    }
}