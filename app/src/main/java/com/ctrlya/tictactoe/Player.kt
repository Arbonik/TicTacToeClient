package com.ctrlya.tictactoe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.ctrlya.tictactoe.canvas.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface Player {
    suspend fun connectToGame(game: Game)

    suspend fun turn(): Flow<Point>

}

class RandomPlayer(
    val area: Point
) : Player {
    override suspend fun connectToGame(game: Game) {

    }

    override suspend fun turn(): Flow<Point> = flow {
        repeat(100) {
            emit(Point(area.x.rangeTo(0).random(), area.y.rangeTo(0).random()))
        }
    }

}

class TicTacToePlayerView(
    context: Context,
    attributeSet: AttributeSet?,
    val scope: CoroutineScope
) :
    TicTacToeView(context, attributeSet), Player {

    val lineDrawer = LineDrawer()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE)

        drawField(canvas)

        lineDrawer.setWinLinePositions(PointF(2f, 0f), PointF(3f, 1f))
        lineDrawer.draw(canvas)

        viewField.forEachIndexed { x, array ->
            array.forEachIndexed { y, it ->
                it.setPosition(x, y)
                it.draw(canvas)
            }
        }
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.d("canvasXXX", canvasX.toString())
//        Log.d("canvasXXX", canvasY.toString())
//        val x = (event.x / scaleFactor - canvasX) / CELL_WIDTH
//        val y = (event.y / scaleFactor - canvasY) / CELL_HEIGHT
//        Log.d("canvasXXX", x.toString())
//        Log.d("canvasXXX", y.toString())

        val point = android.graphics.Point(x.toInt(), y.toInt())

        if (point.x <= 3 && point.y <= 3 && dragging.not()) {
            scope.launch {
                sharedStateFlow.emit(Point(point.x, point.y))
            }
        }

        return super.onTouchEvent(event)
    }

    override suspend fun connectToGame(game: Game) {
    }

    private val sharedStateFlow = MutableSharedFlow<Point>()

    override suspend fun turn(): Flow<Point> = sharedStateFlow
}
