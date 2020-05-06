package uk.ac.bournemouth.ap.dotsandboxes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import uk.ac.bournemouth.ap.dotsandboxeslib.Computer
import uk.ac.bournemouth.ap.dotsandboxeslib.Human
import uk.ac.bournemouth.ap.dotsandboxeslib.LineAlreadyDrawnException
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt


@SuppressLint("ViewConstructor")
class GameView(private val numOfCols: Int, private val numOfRows: Int,
               humanPlayers: Int, computerPlayers: Int, computerDifficulty: Int,
               shufflePlayers: Boolean, context: Context) : View(context) {

    // Paints
    private val backgroundPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.backgroundColor) }

    private val dotPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.dotColor) }

    private val undrawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.undrawnLineColor) }

    private val drawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.drawnLineColor) }

    private val unownedBoxPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.unownedBoxColor) }

    private val defaultTextPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.textColor) }

    // Each player in the player list is assigned the corresponding index as their paint colour.
    private val playerPaints: List<Paint> = listOf(
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player1Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player2Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player3Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player4Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player5Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player6Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player7Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player8Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player9Color) },
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.player10Color) })


    // Dot spacing information - spacings are given values in onDraw, used in gesture detection.
    private var xSpacing: Float = 0F
    private var ySpacing: Float = 0F
    private var spacing: Float = 0F
    private var topBanner: Float = 0F


    // Listeners
    private var gameChangeListener = object : DotsAndBoxesGame.GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    private val gameOverListener = object : DotsAndBoxesGame.GameOverListener {
        override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
            // Display Toast of the player scores.
            var playerScores = "Scores\n"
            for (player in scores) {
                playerScores += if (player.first.toString().contains("Player"))
                    "${player.first}\t\t${player.second}\n"
                else
                    "${player.first}\t\t\t\t${player.second}\n"
            }
            Toast.makeText(context, playerScores, Toast.LENGTH_LONG).show()
        }
    }


    // Game Instance
    var dotsBoxGame: StudentDotsBoxGame
    init {
        // Create the Player objects, add them to the list.
        val players: MutableList<Player> = mutableListOf()
        for (human in 1..humanPlayers) {
            players.add(Human("Player $human"))
        }
        for (bot in 1..computerPlayers) {
            players.add(Computer("Bot $bot", computerDifficulty))
        }

        if(shufflePlayers) { players.shuffle() }

        dotsBoxGame = StudentDotsBoxGame(numOfCols, numOfRows, players)
        dotsBoxGame.addOnGameChangeListener(gameChangeListener)
        dotsBoxGame.addOnGameOverListener(gameOverListener)
    }


    // Gesture Detection
    private val detectInput = GestureDetector(context, GestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detectInput.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean { return true }

        override fun onSingleTapUp(event: MotionEvent): Boolean {

            if(dotsBoxGame.isFinished) { return true }

            val xPos: Float = event.x
            val yPos: Float = event.y

            val columnFloat: Float = xPos / spacing
            val rowFloat: Float = (yPos-topBanner) / spacing

            // Dot Coordinates (Pixels)
            val xDot: Float = floor(columnFloat) * spacing + (spacing / 2)
            val yDot: Float = floor(rowFloat) * spacing + (spacing / 2) + topBanner

            // Used to determine what dot to connect to (up/down/left/right).
            val aboveDot = rowFloat.roundToInt() < rowFloat
            val rightOfDot = columnFloat.roundToInt() > columnFloat

            // Find if the touch was closest to the dot on either the x or y plane.
            val xDelta: Float = abs(xDot - xPos)
            val yDelta: Float = abs(yDot - yPos)

            // Dot Coordinates (Game Grid)
            val xDotAxis: Int = floor(columnFloat).toInt()
            val yDotAxis: Int = floor(rowFloat).toInt()

            try {
                if (minOf(yDelta, xDelta) == xDelta) {
                    if (aboveDot) { dotsBoxGame.lines[xDotAxis, (yDotAxis*2)-1].drawLine() }
                    else { dotsBoxGame.lines[xDotAxis, (yDotAxis*2)+1].drawLine() }
                }
                else {
                    if (rightOfDot) { dotsBoxGame.lines[xDotAxis, yDotAxis*2].drawLine() }
                    else { dotsBoxGame.lines[xDotAxis-1, yDotAxis*2].drawLine() }
                }
            } catch (e: IndexOutOfBoundsException) {
                Toast.makeText(context, "Invalid Move, try again.", Toast.LENGTH_SHORT).show()
            } catch (e: LineAlreadyDrawnException) {
                Toast.makeText(context, "Line already drawn.", Toast.LENGTH_SHORT).show()
            }
            return true
        }
    }


    // Game Interface
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        topBanner = height*0.08F // Top banner is 8% of screen height.
        defaultTextPaint.textSize = topBanner*0.50F

        xSpacing = width / (numOfCols+1).toFloat()
        ySpacing = (height-topBanner) / (numOfRows+1).toFloat()
        spacing = minOf(xSpacing, ySpacing)

        // Scale lines and dots to grid size.
        val dotRadius = spacing / 18
        undrawnLinePaint.strokeWidth = spacing / 20
        drawnLinePaint.strokeWidth = spacing / 20

        // Draw top banner and the background.
        canvas.drawRect(0F, 0F, width.toFloat(), topBanner, drawnLinePaint)
        canvas.drawRect(0F, topBanner, width.toFloat(), height.toFloat(), backgroundPaint)

        val currentPlayerPaint = playerPaints[dotsBoxGame.players.indexOf(dotsBoxGame.currentPlayer)]
        currentPlayerPaint.apply { textSize = topBanner*0.50F }

        // Draw the top banner text, colour it according to the current player.
        if (dotsBoxGame.isFinished) {
            // Display winner(s).
            val winner: List<Player> = dotsBoxGame.winner
            canvas.drawText("$winner ${if (winner.size == 1) "won" else "tied"}!",
                15F, topBanner*0.7F, if(winner.size==1) currentPlayerPaint else defaultTextPaint)
        }
        else {
            // Current player turn.
            canvas.drawText(dotsBoxGame.currentPlayer.toString() + "'s Turn",
                15F, topBanner*0.7F, currentPlayerPaint)

            // Current player score.
            canvas.drawText("Player Score: ${dotsBoxGame.getScores()
                    [dotsBoxGame.players.indexOf(dotsBoxGame.currentPlayer)]}",
                           width-350F, topBanner*0.7F, currentPlayerPaint)
        }
        // Draw game grid.
        for (column in 0..numOfCols) {
            for (row in 0..numOfRows) {
                val x = column * spacing + (spacing/2)
                val y = row * spacing + (spacing/2) + topBanner
                var paint: Paint

                canvas.drawCircle(x, y, dotRadius, dotPaint) // Draw the dot.

                if (column != numOfCols) { // Draw line to dot on the right (excluding dot on the final column).
                    paint = if (dotsBoxGame.lines[column, row * 2].isDrawn) { drawnLinePaint }
                            else { undrawnLinePaint }

                    canvas.drawLine(x+dotRadius, y, (x+spacing-dotRadius), y, paint)

                    // Draw the square - box coordinates are equal to the top-left dot coordinates of said box.
                    if (row != numOfRows) {
                        val boxOwner: Player? = dotsBoxGame.boxes[column, row].owningPlayer

                        paint = if (boxOwner != null) { playerPaints[dotsBoxGame.players.indexOf(boxOwner)] }
                                else { unownedBoxPaint }

                        canvas.drawRect(x+dotRadius, y+dotRadius, x+spacing-dotRadius, y+spacing-dotRadius, paint)
                    }
                }
                if (row != 0) { // Draw line to the dot above (excluding dot on the top row).
                    paint = if (dotsBoxGame.lines[column, (row * 2) - 1].isDrawn) { drawnLinePaint }
                            else { undrawnLinePaint }

                    canvas.drawLine(x, y-dotRadius, x, y-spacing+dotRadius, paint)
                }
            }
        }
    }
}
