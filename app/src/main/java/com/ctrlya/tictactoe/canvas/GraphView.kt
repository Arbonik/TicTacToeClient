package com.ctrlya.tictactoe.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.ScaleGestureDetector
import android.view.View

open class GraphView(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {

    private val paint: Paint = Paint() // Paint object for coloring shapes

    private var initX: Float = 0f // See onTouchEvent
    private var initY: Float = 0f // See onTouchEvent

    protected var canvasX: Float = 0f // x-coord of canvas center
    protected var canvasY: Float = 0f // y-coord of canvas center
    private var dispWidth: Float = 0f // (Supposed to be) width of entire canvas
    private var dispHeight: Float = 0f // (Supposed to be) height of entire canvas

    protected var dragging: Boolean = false// May be unnecessary

    protected var actionUp: Boolean = false
    protected var actionDown: Boolean = false

    private var detector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    protected var scaleFactor: Float = 1f // Zoom level (initial value is 1x)

    private val MIN_ZOOM: Float = 0.1f
    private val MAX_ZOOM: Float = 10f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save() // save() and restore() are used to reset canvas data after each draw
        // Set the canvas origin to the center of the screen only on the first time onDraw is called
        //  (otherwise it'll break the panning code)
//
        canvas.scale(scaleFactor, scaleFactor) // Scale the canvas according to scaleFactor

        // Just draw a bunch of circles (this is for testing panning and zooming
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#000000")
        canvas.translate(canvasX, canvasY)

//        canvas.restore()

        dispWidth = canvas.width.toFloat()
        dispHeight = canvas.height.toFloat()
    }

    // performClick isn't being overridden (should be for accessibility purposes), but it doesn't
    //  really matter here.
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // These two are the coordinates of the user's finger whenever onTouchEvent is called
        val x: Float = event.x
        val y: Float = event.y

        //@TODO: HIGH PRIORITY
        // - Prevent user from scrolling past ends of canvas

        //@TODO: LOW PRIORITY
        // - Add functionality such that initX and initY snap to the position of whichever
        //    finger is up first, be it pointer or main (to prevent jumpiness)
        // - Make sure that when the user zooms in or out the focal point is the midpoint of a line
        //    connecting the main and pointer fingers

        when (event.action and ACTION_MASK) {
            ACTION_DOWN -> {
                // Might not be necessary; check out later
                dragging = true
                // We want to store the coords of the user's finger as it is before they move
                //  in order to calculate dx and dy
                initX = x
                initY = y
            }
            ACTION_MOVE -> {
                // Self explanatory; the difference in x- and y-coords between successive calls to
                //  onTouchEvent
                val dx: Float = x - initX
                val dy: Float = y - initY

                if (dragging) {
                    // Move the canvas dx units right and dy units down
                    // dx and dy are divided by scaleFactor so that panning speeds are consistent
                    //  with the zoom level
                    canvasX += dx / scaleFactor
                    canvasY += dy / scaleFactor

                    invalidate() // Re-draw the canvas

                    // Change initX and initY to the new x- and y-coords
                    initX = x
                    initY = y
                }
            }
            ACTION_POINTER_UP -> {
                // This sets initX and initY to the position of the pointer finger so that the
                //  screen doesn't jump when it's lifted with the main finger still down
                initX = x
                initY = y
            }
            ACTION_UP -> {
                dragging = false
            } // Again, may be unnecessary
        }
        detector.onTouchEvent(event) // Listen for scale gestures (i.e. pinching or double tap+drag
        // Just some useful coordinate data
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Self-explanatory
            scaleFactor *= detector.scaleFactor
            // If scaleFactor is less than 0.5x, default to 0.5x as a minimum. Likewise, if
            //  scaleFactor is greater than 10x, default to 10x zoom as a maximum.
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM))

            invalidate() // Re-draw the canvas

            return true
        }
    }

}