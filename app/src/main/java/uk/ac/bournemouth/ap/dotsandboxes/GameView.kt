package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.example.student.dotsboxgame.Human
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
    private val bgPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.backgroundColor)
    }
    private val dotPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.dotColor)
    }

    private val undrawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.undrawnLineColor)
    }

    private val drawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.drawnLineColor)
    }

    val playerPaints: List<Paint> = listOf(
        Paint().apply {
            style = Paint.Style.FILL
            color = resources.getColor(R.color.player1Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = resources.getColor(R.color.player2Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = resources.getColor(R.color.player3Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = resources.getColor(R.color.player4Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = resources.getColor(R.color.player5Color) })


    // Dot Spacing Information - Given values in onDraw(), used in onSingleTapUp().
    var xSpacing: Float = 0F
    var ySpacing: Float = 0F
    var spacing: Float = 0F


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
    private var dotsBoxGame: StudentDotsBoxGame


    init {
        val players: List<Player> = listOf(Human("Jay"), Human("Player 2"))
        for(player in players.indices) {
            players[player].paintRGB = playerPaints[player].color
        }
        dotsBoxGame = StudentDotsBoxGame(numOfCols, numOfRows, players)
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
            if(dotsBoxGame.currentPlayer is ComputerPlayer) {
                return true
            }
            val xPos: Float = event.x
            val yPos: Float = event.y

            val columnFloat: Float = (xPos / spacing)
            val rowFloat: Float = (yPos / spacing)

            val xDot: Float = floor(columnFloat) * spacing + (spacing / 2)
            val yDot: Float = floor(rowFloat) * spacing + (spacing / 2)

            // Used to determine what line to draw relative to the nearest dot.
            val aboveDot = rowFloat.roundToInt() < rowFloat
            val rightOfDot = columnFloat.roundToInt() > columnFloat

            // Find what axis the touch was closest to, relative to the nearest dot.
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


    // Game Interface
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val dotSize = 10.0F
        xSpacing = width.toFloat() / (numOfCols+1).toFloat()
        ySpacing = height.toFloat() / (numOfRows+1).toFloat()
        spacing = if(xSpacing < ySpacing) xSpacing else ySpacing

        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), bgPaint)

        for(column in 0..numOfCols) {
            for(row in 0..numOfRows) {
                val x = spacing * column + (spacing / 2)
                val y = spacing * row + (spacing / 2)

                canvas.drawCircle(x, y, dotSize, dotPaint) // Draw the dot.

                if(column != numOfCols) { // Draw lines to dot on the right (excluding dots on the final column).
                    if(dotsBoxGame.lines[column, row*2].isDrawn) {
                        canvas.drawLine(x+dotSize, y, (x+dotSize+spacing), y, drawnLinePaint)
                    }
                    else {
                        canvas.drawLine(x+dotSize, y, (x+dotSize+spacing), y, undrawnLinePaint)
                    }
                    // Draw the square between dots. Don't draw it right of the final column, or beneath the final row.
                    if(row != numOfRows) {
                        // Get the box owner.
                        // color = owner.paintRGB
                        // if owner is null, color is background color.
                        val boxOwner = dotsBoxGame.boxes[column, row].owningPlayer
                        var paint: Paint
                        if(boxOwner == null) {
                             paint = Paint().apply {
                                style = Paint.Style.FILL
                                color = resources.getColor(R.color.unownedBoxColor)
                            }
                        }
                        else {
                             paint = Paint().apply {
                                style = Paint.Style.FILL
                                color = boxOwner.paintRGB
                            }
                        }
                        canvas.drawRect(x+dotSize,y+dotSize, x+spacing-dotSize, y+spacing-dotSize, paint)
                    }
                }
                if(row != 0) { // Draw lines to the dot above (excluding dots on the top row).
                    if(dotsBoxGame.lines[column, (row*2)-1].isDrawn) {
                        canvas.drawLine(x, y-dotSize, x, y+dotSize-spacing, drawnLinePaint)
                    }
                    else {
                        canvas.drawLine(x, y-dotSize, x, y+dotSize-spacing, undrawnLinePaint)
                    }
                }
            }
        }
    }
}