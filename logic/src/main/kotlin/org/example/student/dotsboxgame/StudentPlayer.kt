package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer


/** Human Player instance.
 *  Given a name.
 */
class Human(private val name: String): HumanPlayer() {

    override fun toString(): String { return name }
}


/** Bot Player instance.
 *  Given a name and difficulty (Int).
 *  Difficulty determines [makeMove].
 */
class Computer(private val name: String, private val difficulty: Int) : ComputerPlayer() {

    override fun toString(): String { return name }

    override fun makeMove(game: DotsAndBoxesGame) {

        var moveMade = false // Prevents computers making multiple moves in the same turn.

        /**
         *  Base-level difficulty - choose a random line that isn't drawn.
         */


        // TODO: Reference the lines matrix directly??
        // Test this by putting difficulty one one, and using the coordiantes
        // from undrawnLines as an index for lines
        // i.e. lines[undrawnLines.lineX, undrawnLines.lineY]

        fun difficultyOne() {
            val undrawnLines = game.lines.filter { !it.isDrawn }
            if (undrawnLines.isNotEmpty()) {
                undrawnLines.random().drawLine()
            }

        }

        /** Second difficulty - draws lines in boxes with only one other line,
         *  avoiding any two line boxes. If none available, call difficultyOne().
         */
        fun difficultyTwo() {
            for (box in game.boxes) {
                val undrawnLines = box.boundingLines.filter { !it.isDrawn }
                if (undrawnLines.size == 3) {
                    undrawnLines.random().drawLine()
                    moveMade = true
                    break
                }
            }
            if(!moveMade) { difficultyOne() } // Condition not met - fallback to difficulty one.
        }

        /** Third difficulty - completes boxes with three lines drawn.
         *  If none available, call difficultyTwo().
         */
        fun difficultyThree() {
            for (box in game.boxes) {
                val undrawnLines = box.boundingLines.filter { !it.isDrawn }
                // Draw the line if it's the only remaining bounding line undrawn.
                if (undrawnLines.size == 1) {
                    undrawnLines[0].drawLine()
                    moveMade = true
                    break
                }
            }
            if (!moveMade) { difficultyTwo() } // Condition not met - fallback to difficulty two.
        }
        // Select the difficulty.
        when(difficulty) {
            1 -> difficultyOne()
            2 -> difficultyTwo()
            3 -> difficultyThree()
        }
    }
}