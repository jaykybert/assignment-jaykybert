package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import org.example.student.dotsboxgame.Computer
import org.example.student.dotsboxgame.Human
import org.example.student.dotsboxgame.LineAlreadyDrawnException
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

class GameView(private val numOfCols: Int,
               private val numOfRows: Int,
               humanPlayers: Int,
               computerPlayers: Int,
               context: Context?): View(context) {

    // Paints
    private val backgroundPaint: Paint = Paint().apply {
        style = Paint.Style.FILL; resources.getColor(R.color.backgroundColor)
    }
    private val dotPaint: Paint = Paint().apply { // Also used for the current player text.
        style = Paint.Style.FILL; color = ResourcesCompat.getColor(resources, R.color.dotColor, null)
        textSize = 25F; typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val undrawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.undrawnLineColor, null)
    }
    private val drawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.drawnLineColor, null)
    }
    private val unownedBoxPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.unownedBoxColor, null)
    }
    // Each player in the player list is assigned the corresponding index as their paint colour.
    private val playerPaints: List<Paint> = listOf(
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player1Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player2Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player3Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player4Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player5Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player6Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player7Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player8Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player9Color, null) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ResourcesCompat.getColor(resources, R.color.player10Color, null) })

    // Dot Spacing Information - Given values in onDraw(), used in onSingleTapUp().
    private var xSpacing: Float = 0F
    private var ySpacing: Float = 0F
    private var spacing: Float = 0F


    // Listeners
    private var gameChangeListener = object: DotsAndBoxesGame.GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    private val gameOverListener = object: DotsAndBoxesGame.GameOverListener {
        override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
            // Display Toast of the player scores.
            var playerScores = "SCORES\n"
            for(player in scores) {
                // Tab the scores according to the player name.
                playerScores += if(player.first.toString().contains("Player"))
                    "${player.first}\t\t\t${player.second}\n"
                 else
                    "${player.first}\t\t\t\t\t${player.second}\n"
            }
            Toast.makeText(context, playerScores, Toast.LENGTH_LONG).show()
        }
    }


    // Game Instance
    var dotsBoxGame: StudentDotsBoxGame
    init {
        val players: MutableList<Player> = mutableListOf()
        // Create the Player objects, add them to the list.
        for(human in 1..humanPlayers) {
            players.add(Human("Player $human"))
        }
        for(bot in 1..computerPlayers) {
            players.add(Computer("Bot $bot"))
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


    private inner class GestureListener: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean { return true }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            val xPos: Float = event.x
            val yPos: Float = event.y

            val columnFloat: Float = (xPos / spacing)
            val rowFloat: Float = (yPos / spacing)

            val xDot: Float = floor(columnFloat) * spacing + (spacing / 2)
            val yDot: Float = floor(rowFloat) * spacing + (spacing / 2)

            // Used to determine what dot to connect to (up/down/left/right).
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
                    if (aboveDot) { dotsBoxGame.StudentLine(xDotAxis, (yDotAxis * 2) - 1).drawLine() }
                    else { dotsBoxGame.StudentLine(xDotAxis, (yDotAxis * 2) + 1).drawLine() }
                }
                else {
                    if (rightOfDot) { dotsBoxGame.StudentLine(xDotAxis, yDotAxis * 2).drawLine() }
                    else { dotsBoxGame.StudentLine(xDotAxis - 1, yDotAxis * 2).drawLine() }
                }
            }
            catch(e: IndexOutOfBoundsException) {
                Toast.makeText(context, "Invalid Move, try again.", Toast.LENGTH_SHORT).show()
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
        spacing = if(xSpacing < ySpacing) xSpacing
                  else ySpacing

        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), backgroundPaint)

        val winner: List<Player>? = dotsBoxGame.winner
        if(winner != null) { // Game Over - Display Game Winner
            canvas.drawText("$winner ${if(winner.size == 1) "Won!" else "Tied!"}", (numOfCols*spacing)/2, spacing/4, dotPaint)
        }
        else { // Game in progress - Display Player Turn
            canvas.drawText(dotsBoxGame.currentPlayer.toString()+"'s Turn!", (numOfCols*spacing)/2, spacing/4, dotPaint)
        }

        for(column in 0..numOfCols) {
            for(row in 0..numOfRows) {
                val x = spacing * column + (spacing/2)
                val y = spacing * row + (spacing/2)

                canvas.drawCircle(x, y, dotSize, dotPaint) // Draw the dot.

                var paint: Paint

                if(column != numOfCols) { // Draw line to dot on the right (excluding dot on the final column).
                    paint = if(dotsBoxGame.lines[column, row*2].isDrawn) { drawnLinePaint }
                            else { undrawnLinePaint }

                    canvas.drawLine(x+dotSize, y, (x+dotSize+spacing), y, paint)

                    // Draw the square - box coordinates are equal to the top-left dot coordinates of said box.
                    if(row != numOfRows) {
                        val boxOwner: Player? = dotsBoxGame.boxes[column, row].owningPlayer

                        // Set paint equal to the corresponding index in the playerPaints list.
                        paint = if(boxOwner != null) { playerPaints[dotsBoxGame.players.indexOf(boxOwner)] }
                                else { unownedBoxPaint }

                        canvas.drawRect(x+dotSize,y+dotSize, x+spacing-dotSize, y+spacing-dotSize, paint)
                    }
                }
                if(row != 0) { // Draw line to the dot above (excluding dot on the top row).
                    paint = if(dotsBoxGame.lines[column, (row*2)-1].isDrawn) { drawnLinePaint }
                            else { undrawnLinePaint }

                    canvas.drawLine(x, y-dotSize, x, y+dotSize-spacing, paint)
                }
            }
        }
    }
}
