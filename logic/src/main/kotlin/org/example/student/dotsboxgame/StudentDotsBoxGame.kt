package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.*
import java.lang.IndexOutOfBoundsException


// Columns and rows parameters represent boxes, not lines nor dots.
class StudentDotsBoxGame(columns: Int, rows: Int, playerList: List<Player>) : AbstractDotsAndBoxesGame() {

    // TODO("You will need to get players from your constructor")
    override val players: List<Player> = playerList.toList()
    val numOfRows: Int = rows
    val numOfCols: Int = columns


    //TODO("Determine the current player, like keeping the index into the players list").
    // Unsure about this.

    override val currentPlayer: Player get() = players[currentPlayerIdx]

    private var currentPlayerIdx: Int = 0
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


    // NOTE: you may want to me more specific in the box type if you use that type in your class
    // TODO("Create a matrix initialized with your own box type")
    override val boxes: Matrix<DotsAndBoxesGame.Box> = MutableMatrix(columns, rows, ::StudentBox)

    // TODO("Create a matrix initialized with your own line type")
    override var lines: MutableSparseMatrix<DotsAndBoxesGame.Line> = MutableSparseMatrix(columns+1, (rows*2)+1, ::StudentLine) { x, y -> y % 2 != 0 || x < columns }



    //TODO("Provide this getter. Note you can make it a var to do so")
    override val isFinished: Boolean
        get() {
            // The game is finished when all lines have been drawn.
            for(line in lines) {
                if(!line.isDrawn) return false
            }
            return true
            // Maybe also reset the matrices? Redraw the UI?
        }


    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && !isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {

        override var isDrawn: Boolean = false

        // TODO("You need to look up the correct boxes for this to work")
        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                if(lineY % 2 == 0) { // Horizontal Lines
                    val x: Int = lineX
                    val y: Int = lineY / 2 // The y-coord of the box below, -1 for box above.

                    // Edge Cases
                    if(lineY == 0) { // Top row - no box above.
                        return Pair<StudentBox?, StudentBox?>(null, boxes[x, y] as StudentBox)
                    }
                    else if(lineY == numOfRows * 2) { // Bottom row - no box below.
                        return Pair<StudentBox?, StudentBox?>(boxes[x, y-1] as StudentBox, null)
                    }
                    return Pair<StudentBox?, StudentBox?>(boxes[x, y-1] as StudentBox, boxes[x, y] as StudentBox)
                }
                else { // Vertical Lines
                    val x: Int = lineX
                    val y: Int = (lineY - 1) / 2

                    // Edge Cases
                    if(lineX == 0) { // Left column - no box on left.
                        return Pair<StudentBox?, StudentBox?>(null, boxes[x, y] as StudentBox)
                    }
                    else if(lineX == numOfCols) { // Right column - no box on right.
                        return Pair<StudentBox?, StudentBox?>(boxes[x-1, y] as StudentBox, null)
                    }
                    return Pair<StudentBox?, StudentBox?>(boxes[x-1, y] as StudentBox, boxes[x, y] as StudentBox)
                }
            }

        //TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
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
                if(!boxMade) { // Change player if no box was made.
                    changePlayer()
                }
                fireGameChange()
            }


            if(isFinished) {
                val scoreList: MutableList<Pair<Player, Int>> = mutableListOf()
                for(player in players.indices) {
                    val pair: Pair<Player, Int> = Pair(players[player], getScores()[player])
                    scoreList.add(pair)
                }
                fireGameOver(scoreList)
            }
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        //TODO("Provide this getter. Note you can make it a var to do so")
        override var owningPlayer: Player? = null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        // TODO("Look up the correct lines from the game outer class")
        override val boundingLines: Iterable<DotsAndBoxesGame.Line> // Order: Above, Below, Left, Right
            get() = listOf(lines[boxX, boxY*2], lines[boxX, (boxY*2)+2], lines[boxX, (boxY*2)+1], lines[boxX+1, (boxY*2)+1])
    }
}

