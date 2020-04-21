package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.example.student.dotsboxgame.Computer
import org.example.student.dotsboxgame.LineAlreadyDrawnException
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

class GameView(private val numOfCols: Int,
               private val numOfRows: Int,
               context: Context?): View(context) {

    // Paints
    private var bgPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.backgroundColor)
    }
    private var dotPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.dotColor)
    }

    private var undrawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.undrawnLineColor)
    }

    private var drawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.drawnLineColor)
    }


    // Listeners
    private var gameChangeListener = object: DotsAndBoxesGame.GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    private var gameOverListener = object: DotsAndBoxesGame.GameOverListener {
        override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
            // Do something here once the game ends.
            Toast.makeText(context, "END", Toast.LENGTH_SHORT).show()
            invalidate()
        }
    }

    // Game Instance
    private var dotsBoxGame: StudentDotsBoxGame =
        StudentDotsBoxGame(numOfCols, numOfRows, listOf(HumanPlayer(), Computer()))

    init {
        dotsBoxGame.addOnGameChangeListener(gameChangeListener)
        dotsBoxGame.addOnGameOverListener(gameOverListener)
    }

    // Gesture Detection
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

            val aboveDot: Boolean = rowFloat.roundToInt() < rowFloat
            val rightOfDot: Boolean = columnFloat.roundToInt() > columnFloat

            val xDelta: Float = abs(xDot - xPos)
            val yDelta: Float = abs(yDot - yPos)

            val xDotAxis: Int = floor(columnFloat.toDouble()).toInt()
            val yDotAxis: Int = floor(rowFloat.toDouble()).toInt()

            // If deltas are equal, preference is given to first arg. So input is biased
            // (slightly) towards producing horizontal lines.
            try {
                if (minOf(yDelta, xDelta) == xDelta) {
                    if (aboveDot) {
                        dotsBoxGame.StudentLine(xDotAxis, (yDotAxis * 2) - 1).drawLine()
                    }
                    else {
                        dotsBoxGame.StudentLine(xDotAxis, (yDotAxis * 2) + 1).drawLine()
                    }
                }
                else {
                    if (rightOfDot) {
                        dotsBoxGame.StudentLine(xDotAxis, yDotAxis * 2).drawLine()
                    }
                    else {
                        dotsBoxGame.StudentLine(xDotAxis - 1, yDotAxis * 2).drawLine()
                    }
                }
            }
            catch(e: IndexOutOfBoundsException) {
                Toast.makeText(context, "Illegal Move, try again.", Toast.LENGTH_SHORT).show()
            }
            catch(e: LineAlreadyDrawnException) {
                Toast.makeText(context, "Line already drawn.", Toast.LENGTH_SHORT).show()
            }
            return true
        }
    }


    // User Interface
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dotSize = 10.0F

        val xSpacing: Float = width.toFloat() / (numOfCols+1).toFloat()
        val ySpacing: Float = height.toFloat() / (numOfRows+1).toFloat()
        val spacing: Float = if(xSpacing < ySpacing) xSpacing
        else ySpacing
        canvas.drawRect(0.toFloat(), 0.toFloat(), width.toFloat(), height.toFloat(), bgPaint)

        // Until is exclusive, so + 1 for number of dots.
        for(column in 0 until numOfCols + 1) {
            for(row in 0 until numOfRows + 1) {
                val xCoord = spacing * column + (spacing / 2)
                val yCoord = spacing * row + (spacing / 2)
                canvas.drawCircle(xCoord, yCoord, dotSize, dotPaint)

                if(column != numOfCols) {
                    if(dotsBoxGame.lines[column, row*2].isDrawn) {
                        canvas.drawLine(xCoord+dotSize, yCoord, (xCoord+dotSize+spacing), yCoord, drawnLinePaint)
                    }
                    else {
                        canvas.drawLine(xCoord+dotSize, yCoord, (xCoord+dotSize+spacing), yCoord, undrawnLinePaint)
                    }
                }
                if(row != 0) {
                    if(dotsBoxGame.lines[column, (row*2)-1].isDrawn) {
                        canvas.drawLine(xCoord, yCoord-dotSize, xCoord, yCoord+dotSize-spacing, drawnLinePaint)
                    }
                    else {
                        canvas.drawLine(xCoord, yCoord-dotSize, xCoord, yCoord+dotSize-spacing, undrawnLinePaint)
                    }
                }
            }
        }
    }
}