package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class GameView(private val numOfCols: Int,
               private val numOfRows: Int,
               context: Context?): View(context) {

    private var dotsBoxGame: StudentDotsBoxGame = StudentDotsBoxGame(numOfCols, numOfRows, listOf(HumanPlayer(), HumanPlayer()))

    private var bgPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }
    private var dotPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    // Choose the smallest spacing to allow portrait/landscape use.
    private val xSpacing: Float = width.toFloat() / (numOfCols+1).toFloat()
    private val ySpacing: Float = height.toFloat() / (numOfRows+1).toFloat()
    private val spacing: Float = if(xSpacing < ySpacing) xSpacing
    else ySpacing

    // TODO: Add listener implementation.

    // GESTURE DETECTION
    private val detectInput = GestureDetector(context, GestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detectInput.onTouchEvent(event) || super.onTouchEvent(event)
}

    inner class GestureListener: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean { return true }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            val xPos: Int = event.x.toInt()
            val yPos: Int = event.y.toInt()
            val closestCol: Float = xPos / spacing
            val closestRow: Float = yPos / spacing

            Toast.makeText(context,"X:$xPos COL:$closestCol Y:$yPos ROW:$closestRow", Toast.LENGTH_SHORT).show()
            // TODO: Find the closest dots and connect them.
            // METHOD:
            // 1. Determine column of the tap.
            // 2. Determine if tap is left or right of the centre of the column.
            //      IF left, connect to left dot (if legal).
            //      IF right, connect to right dot (if legal).
            //      IF above dot (within certain value range), connect to dot above (if legal).
            //      IF below dot (within certain value range), connect to dot below (if legal).
            return true
        }
    }


    // USER INTERFACE
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, bgPaint)

        // Columns and Rows are box columns/rows. Add one to each for dots columns/rows.
        for(column in 0 until numOfCols + 1) {
            for(row in 0 until numOfRows + 1) {
                val xCoord = spacing * column + (spacing / 2)
                val yCoord = spacing * row + (spacing / 2)
                canvas.drawCircle(xCoord, yCoord, (spacing / 10), dotPaint)
            }
        }
    }
}