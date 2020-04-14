package org.example.student.dotsboxgame


class LineAlreadyDrawnException(private val lineX: Int, private val lineY: Int): Exception() {

    override fun toString(): String {
        return "The line at coordinates ($lineX,$lineY) has already been drawn."
    }
}