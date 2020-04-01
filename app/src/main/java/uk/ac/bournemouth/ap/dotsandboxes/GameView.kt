package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class GameView(private val numOfCols: Int, private val numOfRows: Int, players: List<Player>, context: Context?): View(context) {

    private var dotsBoxGame: StudentDotsBoxGame = StudentDotsBoxGame(numOfCols, numOfRows, players)

    private var bgPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }
    private var dotPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    // TODO: Add listener implementation.

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

    // TODO: Add gesture detection - determine what line was chosen relative to the dots.
}