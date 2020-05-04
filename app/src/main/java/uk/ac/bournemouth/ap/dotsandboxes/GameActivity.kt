package uk.ac.bournemouth.ap.dotsandboxes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get game parameters.
        val humanPlayers = intent.getIntExtra(HUMANS, 1)
        val computerPlayers = intent.getIntExtra(BOTS, 1)
        val computerDifficulty = intent.getIntExtra(DIFFICULTY, 1)
        val columns = intent.getIntExtra(COLUMNS, 3)
        val rows = intent.getIntExtra(ROWS, 3)
        val shuffle = intent.getBooleanExtra(SHUFFLE, false)
        val gameView = GameView(columns, rows, humanPlayers, computerPlayers, computerDifficulty, shuffle, this)
        setContentView(gameView)
    }
}