package uk.ac.bournemouth.ap.dotsandboxeslib


/** Custom exception that should be used when a Line object is created with the same
 *  coordinates as one which already exists inside the Lines matrix.
 */
class LineAlreadyDrawnException(private val lineX: Int, private val lineY: Int): Exception() {

    override fun toString(): String {
        return "The line at coordinates ($lineX,$lineY) has already been drawn."
    }
}