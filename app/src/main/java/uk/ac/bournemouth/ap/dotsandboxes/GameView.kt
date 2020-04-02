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
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class GameView(private val numOfCols: Int,
               private val numOfRows: Int,
               context: Context?): View(context) {

    //private var dotsBoxGame: StudentDotsBoxGame = StudentDotsBoxGame(numOfCols, numOfRows)

    private var bgPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }
    private var dotPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    // TODO: Add listener implementation.
    // TODO: Figure out how to pass a list of players to StudentDotsBoxGame.

    private val detectInput = GestureDetector(context, GestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detectInput.onTouchEvent(event) || super.onTouchEvent(event)
}

    inner class GestureListener: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean { return true }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            Toast.makeText(context,"Tap Confirmed", Toast.LENGTH_SHORT).show()

            // TODO: Find the closest dots and connect them.

            return true
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()

        // Choose the smallest-sized diameter to allow for the game
        // to be played in landscape and portrait.
        val xDiameter: Float = viewWidth / (numOfCols+1).toFloat()
        val yDiameter: Float = viewHeight / (numOfRows+1).toFloat()
        val chosenDiameter: Float = if(xDiameter < yDiameter) xDiameter
        else yDiameter

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, bgPaint)

        // Columns and Rows are box columns/rows. Add one to each for dots columns/rows.
        for(column in 0 until numOfCols + 1) {
            for(row in 0 until numOfRows + 1) {
                val xCoord = chosenDiameter * column + (chosenDiameter / 2)
                val yCoord = chosenDiameter * row + (chosenDiameter / 2)
                canvas.drawCircle(xCoord, yCoord, (chosenDiameter / 10), dotPaint)
            }
        }
    }
}