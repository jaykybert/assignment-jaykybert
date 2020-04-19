package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer

class Human(private val name: String): HumanPlayer() {

    override fun toString(): String { return name }
}


class Computer: ComputerPlayer() {

    /**
     * Make a move for the player. This is normally called by the game, not directly. If the player
     * is set on multiple games (and used to make moves on these separate games), the implementation
     * is required to be implemented to allow this to work correctly (if it records game specific
     * information this needs to be separated from any other game data). The easiest solution is to
     * create a different player instance for each game. It is the responsibility of the game to
     * call this function again if another move can be made in the same turn, this function is only
     * for individual moves.
     *
     * @param game The game to make a move on.
     */
    override fun makeMove(game: DotsAndBoxesGame) {
        // Go through lines matrix, find the first un-drawn line.
        for(line in game.lines) {
            if(!line.isDrawn)
                line.drawLine()
        }
    }
}