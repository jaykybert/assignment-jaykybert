package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.*
import java.lang.IndexOutOfBoundsException


/** Instance of a game.
 *  Columns and rows parameters represent boxes - not dots, nor lines.
 */
class StudentDotsBoxGame(columns: Int, rows: Int, playerList: List<Player>) : AbstractDotsAndBoxesGame() {

    // Matrices
    override val boxes: Matrix<DotsAndBoxesGame.Box> = MutableMatrix(columns, rows, ::StudentBox)
    override var lines: MutableSparseMatrix<DotsAndBoxesGame.Line> = MutableSparseMatrix(columns+1, (rows*2)+1, ::StudentLine) { x, y -> y % 2 != 0 || x < columns }


    // Player Tracking
    override val players: List<Player> = playerList.toList()
    override val currentPlayer: Player get() = players[currentPlayerIdx] // Need error handling for 0 player games.

    private var currentPlayerIdx: Int = 0

    /** Increments the index position which is used by the
     *  [currentPlayer] val. Resets it to 0 at the end of [players].
     *  Also initiates computer turns.
     */
    private fun changePlayer() {
        if(currentPlayerIdx == players.size-1) {
            currentPlayerIdx = 0
        }
        else {
            currentPlayerIdx++
        }
        if(players[currentPlayerIdx] is ComputerPlayer) {
            playComputerTurns()
        }
    }

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && !isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    // Start the game if the first player is a computer.
    init {
        playComputerTurns()
    }


    override val isFinished: Boolean
        get() {
            // Game is finished when all lines are drawn or all boxes are owned.
            if(lines.any { !it.isDrawn }) {
                return false
            }
            return true
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
                if(lineY % 2 == 0) { // Horizontal Lines - Adjacent boxes are above/below.
                    val x: Int = lineX
                    val y: Int = lineY / 2 // This is the y of the box below, -1 for box above.

                    // Edge Cases
                    if(lineY == 0) { // First Row - No box above.
                        return Pair<StudentBox?, StudentBox?>(null, boxes[x, y] as StudentBox)
                    }
                    else if(lineY == lines.maxHeight-1) { // Bottom Row - No box below.
                        return Pair<StudentBox?, StudentBox?>(boxes[x, y-1] as StudentBox, null)
                    }
                    return Pair<StudentBox?, StudentBox?>(boxes[x, y-1] as StudentBox, boxes[x, y] as StudentBox)
                }
                else { // Vertical Lines - Adjacent boxes are left/right.
                    val x: Int = lineX
                    val y: Int = (lineY - 1) / 2

                    // Edge Cases
                    if(lineX == 0) { // Left-most Column - No box on left.
                        return Pair<StudentBox?, StudentBox?>(null, boxes[x, y] as StudentBox)
                    }
                    else if(lineX == lines.maxWidth-1) { // Right-most Column - No box on right.
                        return Pair<StudentBox?, StudentBox?>(boxes[x-1, y] as StudentBox, null)
                    }
                    return Pair<StudentBox?, StudentBox?>(boxes[x-1, y] as StudentBox, boxes[x, y] as StudentBox)
                }
            }


        override fun drawLine() {
            if (lines[lineX, lineY].isDrawn) {
                throw LineAlreadyDrawnException(lineX, lineY)
            }
            else {
                if(!lines.isValid(lineX, lineY)) {
                    throw IndexOutOfBoundsException()
                }
                lines[lineX, lineY] = this
                isDrawn = true
                // See if the line completes the adjacent boxes.
                val box1: StudentBox? = this.adjacentBoxes.first
                val box2: StudentBox? = this.adjacentBoxes.second
                var boxMade = false
                if(box1 != null) {
                    if(box1.boundingLines.all { it.isDrawn }) {
                                // Set owning player.
                                for(box in boxes) {
                                    if(box1 == box) {
                                        box.owningPlayer = currentPlayer
                                        boxMade = true
                                    }
                        }
                    }
                }
                if(box2 != null) {
                    if(box2.boundingLines.all { it.isDrawn }) {
                        for(box in boxes) {
                            if(box2 == box) {
                                box.owningPlayer = currentPlayer
                                boxMade = true
                            }
                        }
                    }
                }
                fireGameChange()
                if(!boxMade) { // Change the player if no box was made.
                    changePlayer()
                }
            }
            // Check finish condition, retrieve final scores.
            if(isFinished) {
                val scoreList: MutableList<Pair<Player, Int>> = mutableListOf()
                for(player in players.indices) {
                    scoreList.add(Pair(players[player], getScores()[player]))
                }
                fireGameOver(scoreList)
            }
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line> // Line Order: Above, Below, Left, Right
            get() = listOf(lines[boxX, boxY*2],
                           lines[boxX, (boxY*2)+2],
                           lines[boxX, (boxY*2)+1],
                           lines[boxX+1, (boxY*2)+1])
    }
}
