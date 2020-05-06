package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.*
import java.lang.IndexOutOfBoundsException


/** Instance of a game.
 *  Columns and rows parameters represent boxes - not dots, nor lines.
 */
class StudentDotsBoxGame(columns: Int, rows: Int, playerList: List<Player>) : AbstractDotsAndBoxesGame() {

    // Matrices
    override val boxes: Matrix<StudentBox> = MutableMatrix(columns, rows, ::StudentBox)
    override var lines: MutableSparseMatrix<StudentLine> =
        MutableSparseMatrix(columns+1, rows*2+1, ::StudentLine) { x, y -> y % 2 != 0 || x < columns }


    // Player Tracking
    override val players: List<Player> = playerList.toList()
    private var currentPlayerIdx: Int = 0
    override val currentPlayer: Player get() = players[currentPlayerIdx]

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && !isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    // Human may not be the first to make a move due to bot-only games and shuffled player lists.
    init {
        playComputerTurns()
    }

    override val isFinished: Boolean
        get() = lines.all { it.isDrawn }


    /**
     * Attach the scores from [getScores] to the respective [Player]s, and return as a list of pairs.
     */
    fun playerScores(): List<Pair<Player, Int>> {
        val playerScoreList: MutableList<Pair<Player, Int>> = mutableListOf()
        val scoreList = getScores()
        for(player in players.indices) {
            playerScoreList.add(Pair(players[player], scoreList[player]))
        }
        return playerScoreList
    }


    /**
     *  A list of [Player] with the greatest score.
     */
    val winner: List<Player>
        get() {
            val scores = playerScores()
            val winnerList: MutableList<Player> = mutableListOf()
            var winningScore = 0

            for(player in scores) {
                if(player.second > winningScore) {
                    winningScore = player.second
                    winnerList.clear()
                    winnerList.add(player.first)
                }
                else if(player.second == winningScore) {
                    winnerList.add(player.first)
                }
            }
            return winnerList
        }


    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {

        override var isDrawn: Boolean = false

        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                // Horizontal Lines - adjacent boxes are above/below.
                if (lineY % 2 == 0) {
                    val x: Int = lineX
                    val y: Int = lineY / 2 // This is y of the box below, -1 for box above.

                    return when (lineY) {
                        0                   -> Pair(null, boxes[x, y]) // No box above.
                        lines.maxHeight - 1 -> Pair(boxes[x, y - 1], null) // No box below.
                        else                -> Pair(boxes[x, y - 1], boxes[x, y])
                    }
                }
                else {
                    val x: Int = lineX
                    val y: Int = (lineY - 1) / 2

                    return when (lineX) {
                        0                  -> Pair(null, boxes[x, y]) // No box on the left.
                        lines.maxWidth - 1 -> Pair(boxes[x - 1, y], null) // No box on the right.
                        else               -> Pair(boxes[x - 1, y], boxes[x, y])
                    }
                }
            }


        override fun drawLine() {
            if (lines[lineX, lineY].isDrawn) {
                throw LineAlreadyDrawnException(lineX, lineY)
            }

            if(!lines.isValid(lineX, lineY)) {
                throw IndexOutOfBoundsException()
            }
            isDrawn = true
            var boxMade = false
            val box1: StudentBox? = adjacentBoxes.first
            val box2: StudentBox? = adjacentBoxes.second

            // Check if the all the boxes' bounding lines are now drawn.
            if(box1 != null && box1.boundingLines.all { it.isDrawn }) {
                boxes[box1.boxX, box1.boxY].owningPlayer = currentPlayer
                boxMade = true
            }
            if(box2 != null && box2.boundingLines.all { it.isDrawn }) {
                boxes[box2.boxX, box2.boxY].owningPlayer = currentPlayer
                boxMade = true
            }
            fireGameChange()

            // Check finish condition, retrieve scores.
            if(isFinished) {
                fireGameOver(playerScores())
            }

            else if(!boxMade) { // Change the player if no box was made.
                currentPlayerIdx = (currentPlayerIdx+1) % players.size
                playComputerTurns()
            }
        }
    }

     inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null

        /** This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         *  Line Order: Above, Below, Left, Right
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() = listOf(lines[boxX, boxY*2],
                           lines[boxX, (boxY*2)+2],
                           lines[boxX, (boxY*2)+1],
                           lines[boxX+1, (boxY*2)+1])
    }
}
