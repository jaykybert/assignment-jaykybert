package uk.ac.bournemouth.ap.dotsandboxeslib


/** Human Player instance.
 *  Given a name.
 */
class Human(private val name: String="Player"): HumanPlayer() {

    override fun toString(): String { return name }
}


/** Bot Player instance.
 *  Given a name and difficulty.
 *  Difficulty determines [makeMove].
 */
class Computer(private val name: String="Bot", private val difficulty: Int=1) : ComputerPlayer() {

    override fun toString(): String { return name }

    /**
     *  Determine what line to draw. Three difficulties that fall to the
     *  next lowest difficulty if the condition isn't met.
     */
    override fun makeMove(game: DotsAndBoxesGame) {

        var moveMade = false // Prevent computers making multiple moves in the same turn.

        /**
         *  Base-level difficulty - choose a random line that isn't drawn.
         */
        fun difficultyOne() {
            val undrawnLines = game.lines.filter { !it.isDrawn }
            if (undrawnLines.isNotEmpty()) {
                undrawnLines.random().drawLine()
            }

        }

        /** Second difficulty - draw line in boxes that have 0 or 1 lines currently drawn,
         *  avoiding any two line boxes. If none available, call difficultyOne().
         */
        fun difficultyTwo() {
            for (box in game.boxes) {
                val undrawnLines = box.boundingLines.filter { !it.isDrawn }
                if (undrawnLines.size >= 3 ) {
                    undrawnLines.random().drawLine()
                    moveMade = true
                    break
                }
            }
            if(!moveMade) { difficultyOne() } // Move not made - fallback to difficulty one.
        }

        /** Third difficulty - complete boxes with three lines already drawn.
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
            if (!moveMade) { difficultyTwo() } // Move not made - fallback to difficulty two.
        }
        // Select the difficulty.
        when(difficulty) {
            1 -> difficultyOne()
            2 -> difficultyTwo()
            3 -> difficultyThree()
        }
    }
}
