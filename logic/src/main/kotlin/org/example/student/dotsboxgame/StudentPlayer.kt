package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer

class Human(private val name: String): HumanPlayer() {

    override fun toString(): String { return name }
}


class Computer: ComputerPlayer() {

    override fun makeMove(game: DotsAndBoxesGame) {
        // Go through lines matrix, find the first un-drawn line.
        for(line in game.lines) {
            if(!line.isDrawn) {
                line.drawLine()
                break
            }
        }
    }
}