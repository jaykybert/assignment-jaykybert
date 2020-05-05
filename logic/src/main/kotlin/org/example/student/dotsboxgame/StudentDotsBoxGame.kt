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

    // Bot-only games shouldn't wait for input to start.
    init {
        playComputerTurns()
    }

    // Game is finished when all lines are drawn/all boxes are owned.
    override val isFinished: Boolean
        get() = lines.all { it.isDrawn }


    /**
     * Attach the score to the respective [Player], and return as a list of pairs.
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
                if(lineY % 2 == 0) { // Horizontal Lines - adjacent boxes are above/below.
                    val x: Int = lineX
                    val y: Int = lineY / 2 // This is the y of the box below, -1 for box above.

                    // Edge Cases
                    if(lineY == 0) { // First Row - no box above.
                        return Pair(null, boxes[x, y])
                    }
                    else if(lineY == lines.maxHeight-1) { // Bottom Row - no box below.
                        return Pair(boxes[x, y-1], null)
                    }
                    return Pair(boxes[x, y-1], boxes[x, y])
                }
                else { // Vertical Lines - adjacent boxes are left/right.
                    val x: Int = lineX
                    val y: Int = (lineY - 1) / 2

                    // Edge Cases
                    if(lineX == 0) { // Left-most Column - no box on left.
                        return Pair(null, boxes[x, y])
                    }
                    else if(lineX == lines.maxWidth-1) { // Right-most Column - no box on right.
                        return Pair(boxes[x-1, y], null)
                    }
                    return Pair<StudentBox?, StudentBox?>(boxes[x-1, y], boxes[x, y])
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

            else if(!boxMade) { // Change the player if no box was made. // was else if
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
