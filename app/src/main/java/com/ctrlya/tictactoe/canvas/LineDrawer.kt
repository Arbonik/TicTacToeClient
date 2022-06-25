package com.ctrlya.tictactoe.canvas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import java.lang.Math.abs

class LineDrawer() : Drawn() {

    var p1 = PointF(0f, 0f)
    var p2 = PointF(0f, 0f)

    var type = LineType.VLINE

//    var winCount = 0
//
//    val END_X: Float
//        get() = 270f * (winCount + eXPlus)
//
//    val END_Y: Float
//        get() = 270f * (winCount + eYPlus)
//
//    val stopX: Float
//        get() = minOf(animationCurrentTime.toFloat() / animationTime * END_X, END_X)
//
//    val stopY: Float
//        get() = minOf(animationCurrentTime.toFloat() / animationTime * END_Y, END_Y)
//
//    private var sXPlus = 0
//    private var sYPlus = 0
//    private var eXPlus = 0
//    private var eYPlus = 0
//
//    private var lineType: LineType = LineType.LDIAG
//
//    override fun draw(canvas: Canvas) {
//
//        val p = Paint().apply {
//            strokeWidth = 20f
//            color = Color.RED
//            strokeCap = Paint.Cap.ROUND
//        }
//
//        lineType = getLineType()
//        setPluses()
//
//
////        canvas.drawLine(
////            (p1.x + sXPlus) * 270f,
////            (p1.y + sYPlus) * 270f,
////            (p2.x + eXPlus) * 270f,
////            (p2.y + eYPlus) * 270f,
////            p
////        )
//
//        Log.d("AAA", p1.toString())
//        Log.d("AAA", p2.toString())
//        Log.d("AAA", kotlin.math.abs(p1.x - p1.y).toString())
//        Log.d("AAA", END_X.toString())
//        Log.d("BBB", stopX.toString())
//        Log.d("BBB", stopY.toString())
//
//        canvas.drawLine(
//            (p1.x + sXPlus) * 270f,
//            (p1.y + sYPlus) * 270f,
//            stopX,
//            stopY,
//            p
//        )
//    }
//
//    fun setWinLinePositions(startPoint: PointF, endPoint: PointF) {
//        p1 = startPoint
//        p2 = endPoint
//    }
//
//    private fun setPluses() {
//        when (lineType) {
//            LineType.LDIAG -> {
//                eXPlus = 1
//                eYPlus = 1
//            }
//            LineType.RDIAG -> {
//                eYPlus = 1
//                sXPlus = 1
//            }
//        }
//    }
//
//    private fun getLineType(): LineType {
//        if (p1.x == p2.x) {
//            return LineType.HLINE
//        } else if (p1.y == p2.y) {
//            return LineType.VLINE
//        } else if (p1.x < p2.x) {
//            return LineType.LDIAG
//        } else {
//            return LineType.RDIAG
//        }
//    }
//
//    override fun setPosition(x: Int, y: Int) {
//
//    }

    private fun getLineType(): LineType {
        if (p1.x == p2.x) {
            return LineType.VLINE
        } else if (p1.y == p2.y) {
            return LineType.HLINE
        } else if (p1.x < p2.x) {
            return LineType.LDIAG
        } else {
            return LineType.RDIAG
        }
    }

    fun setWinLinePositions(startPoint: PointF, endPoint: PointF) {
        p1.x = startPoint.x + 1
        p1.y = startPoint.y + 1
        p2.x = endPoint.x + 1
        p2.y = endPoint.y + 1
    }

    override fun draw(canvas: Canvas) {
        val p = Paint().apply {
            color = Color.RED
            strokeWidth = 25f
            strokeCap = Paint.Cap.ROUND
        }
//        canvas.drawLine(
//            (cWidth * (start.x)).toFloat(),
//            (cHeight * (start.y - 1)).toFloat(),
//            (cWidth * (end.x - 1)).toFloat(),
//            (cHeight * (end.y)).toFloat(),
//            p
//        )
        var sXMinus = 0
        var sYMinus = 0
        var eXMinus = 0
        var eYMinus = 0

        type = getLineType()

        var diag = false

        when (type) {
            LineType.LDIAG -> {
                sXMinus = 1
                sYMinus = 1
                diag = true
            }
            LineType.RDIAG -> {
                sYMinus = 1
                eXMinus = 1
                diag = true
            }
            LineType.HLINE -> {
                sYMinus = 1
                eYMinus = 1
                sXMinus = 1
            }
            LineType.VLINE -> {
                sXMinus = 1
                eXMinus = 1
                sYMinus = 1
            }
        }
        if (diag) {
            canvas.drawLine(
                (270f * (p1.x - sXMinus)).toFloat(),
                (270f * (p1.y - sYMinus)).toFloat(),
                (270f * (p2.x - eXMinus)).toFloat(),
                (270f * (p2.y - eYMinus)).toFloat(),
                p
            )
        } else if (type == LineType.VLINE) {
            canvas.drawLine(
                (270f * (p1.x - sXMinus)) + 135f,
                (270f * (p1.y - sYMinus)),
                (270f * (p2.x - eXMinus)) + 135f,
                (270f * (p2.y - eYMinus)),
                p
            )
        } else {
            canvas.drawLine(
                (270f * (p1.x - sXMinus)),
                (270f * (p1.y - sYMinus)) + 135f,
                (270f * (p2.x - eXMinus)),
                (270f * (p2.y - eYMinus)) + 135f,
                p
            )
        }
    }

    override fun setPosition(x: Int, y: Int) {
        TODO("Not yet implemented")
    }
}

enum class LineType {
    LDIAG,
    RDIAG,
    HLINE,
    VLINE
}