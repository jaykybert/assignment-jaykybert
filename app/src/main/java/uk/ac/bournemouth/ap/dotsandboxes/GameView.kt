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
import kotlin.math.roundToInt

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


    // TODO: Add listener implementation.

    // GESTURE DETECTION
    private val detectInput = GestureDetector(context, GestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detectInput.onTouchEvent(event) || super.onTouchEvent(event)
}

    inner class GestureListener: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean { return true }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            val xSpacing: Float = width.toFloat() / (numOfCols+1).toFloat()
            val ySpacing: Float = height.toFloat() / (numOfRows+1).toFloat()
            val spacing: Float = if(xSpacing < ySpacing) xSpacing else ySpacing

            val xPos: Int = event.x.toInt()
            val yPos: Int = event.y.toInt()

            val columnFloat: Float = (xPos / spacing)
            val columnTapped: Int = columnFloat.roundToInt()
            val rowFloat: Float = (yPos / spacing)
            val rowTapped: Int = rowFloat.roundToInt()

            if (columnTapped > columnFloat) { // Rounded upwards - right of dot.
                // Compare horizontal distance with vertical - find smallest.
            }
            else { // Rounded downwards - left of dot.
                // Compare horizontal distance with vertical - find smallest.
            }

            Toast.makeText(context,"COL:$columnTapped ROW:$rowTapped", Toast.LENGTH_SHORT).show()
            // TODO: Find the closest dots and connect them.

            return true
        }
    }


    // USER INTERFACE
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val xSpacing: Float = width.toFloat() / (numOfCols+1).toFloat()
        val ySpacing: Float = height.toFloat() / (numOfRows+1).toFloat()
        val spacing: Float = if(xSpacing < ySpacing) xSpacing
        else ySpacing

        canvas.drawRect(0.toFloat(), 0.toFloat(), width.toFloat(), height.toFloat(), bgPaint)

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