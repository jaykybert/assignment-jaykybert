package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import java.util.logging.Handler


class Human(private val name: String): HumanPlayer() {

    override fun toString(): String { return name }
}


class Computer(): ComputerPlayer() {

    override fun toString(): String { return "Bot" }

    // TODO: Add AI
    override fun makeMove(game: DotsAndBoxesGame) {
        // Default Implementation - next available line.
        for(line in game.lines) {
            if(!line.isDrawn) {
                line.drawLine()
                break
            }
        }
    }
}