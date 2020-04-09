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
import uk.ac.bournemouth.ap.dotsandboxeslib.*
import kotlin.math.abs
import kotlin.math.floor
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


    // TODO: Add listener onGameOver implementation.
    var gameListener = object: DotsAndBoxesGame.GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    init {
        dotsBoxGame.addOnGameChangeListener(gameListener)
    }


    // GESTURE DETECTION
    private val detectInput = GestureDetector(context, GestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detectInput.onTouchEvent(event) || super.onTouchEvent(event)
}

    inner class GestureListener: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            val xSpacing: Float = width.toFloat() / (numOfCols + 1).toFloat()
            val ySpacing: Float = height.toFloat() / (numOfRows + 1).toFloat()
            val spacing: Float = if (xSpacing < ySpacing) xSpacing else ySpacing

            val xPos: Int = event.x.toInt()
            val yPos: Int = event.y.toInt()

            val columnFloat: Float = (xPos / spacing)
            val rowFloat: Float = (yPos / spacing)

            val xDot: Float = floor(columnFloat) * spacing + (spacing / 2)
            val yDot: Float = floor(rowFloat) * spacing + (spacing / 2)

            val aboveDot: Boolean = rowFloat.roundToInt() > rowFloat
            val rightOfDot: Boolean = columnFloat.roundToInt() > columnFloat

            val xDelta: Float = abs(xDot - xPos)
            val yDelta: Float = abs(yDot - yPos)

            //
            val xDotAxis: Int = floor(columnFloat.toDouble()).toInt()
            val yDotAxis: Int = floor(rowFloat.toDouble()).toInt()

            if (xDotAxis > numOfCols + 1 || yDotAxis > numOfRows + 1) {
                Toast.makeText(context, "Invalid Input. Try Again.", Toast.LENGTH_SHORT).show()
                return true
            }
            else {
                Toast.makeText(context, "x:$xDotAxis, y:$yDotAxis", Toast.LENGTH_SHORT).show()
                // If deltas are equal, preference is given to first arg. So input is biased
                // (slightly) towards producing horizontal lines.
                // TODO: Call drawLine() on the StudentLine objects.
                if (minOf(yDelta, xDelta) == xDelta) {
                    if (aboveDot) {
                        // Go to dot above.
                        dotsBoxGame.StudentLine(xDotAxis, (yDotAxis * 2) - 1)
                    } else {
                        // Go to dot below.
                        dotsBoxGame.StudentLine(xDotAxis, (yDotAxis * 2) + 1)
                    }
                } else {
                    if (rightOfDot) {
                        // Go to right dot.
                        dotsBoxGame.StudentLine(xDotAxis, yDotAxis * 2)
                    } else {
                        // Got to left dot.
                        dotsBoxGame.StudentLine(xDotAxis - 1, yDotAxis * 2)
                    }
                }
                return true
            }
        }
    }


    // USER INTERFACE
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dotSize: Float = 10.0F

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
                canvas.drawCircle(xCoord, yCoord, dotSize, dotPaint)
            }
        }
    }
}