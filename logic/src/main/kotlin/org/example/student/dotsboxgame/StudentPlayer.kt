package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer

class Human(private val name: String): HumanPlayer() {

    override fun toString(): String { return name }
}


class Computer(difficulty: Int): ComputerPlayer() {

    override fun makeMove(game: DotsAndBoxesGame) {

        // Difficulty 1: Random Line Selection
        fun difficultyOne() {
            val linesShuffled = game.lines.shuffled()
            for (line in linesShuffled) {
                if (!line.isDrawn) {
                    line.drawLine()
                    break
                }
            }
        }

        // Difficulty 2: Play 3 Line Boxes, fallback to difficulty 1.
        fun difficultyTwo() {
            for(box in game.boxes) {
                var lineCount = 0
                for(line in box.boundingLines) {
                    if(line.isDrawn) {
                        lineCount ++
                    }
                    if(lineCount == 3 && !line.isDrawn) { // Check if the final line is not drawn.
                        line.drawLine()
                    }
                }
            }
            difficultyOne()
        }

        // Difficulty 3: Avoid 2 line boxes, fallback to difficulty 2.
    }
}