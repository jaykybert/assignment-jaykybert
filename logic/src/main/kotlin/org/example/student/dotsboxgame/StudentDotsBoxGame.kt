package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.*


// Columns and rows parameters represent boxes, not lines nor dots.
class StudentDotsBoxGame(columns: Int, rows: Int, playerList: List<Player>) : AbstractDotsAndBoxesGame() {

    // TODO("You will need to get players from your constructor")
    override val players: List<Player> = playerList.toMutableList()


    //TODO("Determine the current player, like keeping the index into the players list").
    // Unsure about this.
    private var currentPlayerIdx: Int = 0
    override val currentPlayer: Player get() {
        if(currentPlayerIdx == players.size) currentPlayerIdx = 0
        return players[currentPlayerIdx]
    }


    // NOTE: you may want to me more specific in the box type if you use that type in your class
    // TODO("Create a matrix initialized with your own box type")
    override val boxes: Matrix<DotsAndBoxesGame.Box> = MutableMatrix(columns, rows, ::StudentBox)

    // TODO("Create a matrix initialized with your own line type")
    // Column+1 since the total number of column indexes is one greater than the number of boxes.
    //  - this is due to vertical lines, horizontal lines are equal to the number of boxes - hence array validation being needed.

    // (Row*2)+1 since every time there is a change from horizontal to vertical we consider it a new row.
    // - therefore one box has 3 rows (top horizontal line, middle vertical line, bottom horizontal line).
    override var lines: MutableSparseMatrix<DotsAndBoxesGame.Line> = MutableSparseMatrix(columns+1, (rows*2)+1, ::StudentLine)


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
        while (current is ComputerPlayer && ! isFinished) {
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

        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                TODO("You need to look up the correct boxes for this to work")
            }

        //TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
        override fun drawLine() {
            if (!isDrawn) {
                lines[lineX, lineY] = this
                isDrawn = true
                // TODO - Check here if it forms a box. If not, increment player count.
                currentPlayerIdx++
            }
            else {
                throw LineAlreadyDrawnException(lineX, lineY)
            }
            fireGameChange()
            // fireGameOver(params)
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        //TODO("Provide this getter. Note you can make it a var to do so")
        override val owningPlayer: Player?
            get() = boxes[boxX, boxY].owningPlayer

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() = TODO("Look up the correct lines from the game outer class")

    }
}
