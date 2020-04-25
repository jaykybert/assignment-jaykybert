package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player


const val PLAYERS = "uk.ac.bournemouth.ap.dotsandboxes.PLAYERS"
const val SCORES = "uk.ac.bournemouth.ap.dotsandboxes.SCORES"


class GameActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get game parameters from MainActivity.
        val humanPlayers = intent.getIntExtra(HUMANS, 1)
        val computerPlayers = intent.getIntExtra(BOTS, 1)
        val columns = intent.getIntExtra(COLUMNS, 3)
        val rows = intent.getIntExtra(ROWS, 3)

        val gameView = GameView(columns, rows, humanPlayers, computerPlayers, this)
        setContentView(gameView)
        gameView.dotsBoxGame.addOnGameOverListener(gameOverListener)
    }


    // Listen for game over. Start popup.
     var gameOverListener = object: DotsAndBoxesGame.GameOverListener {
        override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
            // Do something here once the game ends.

            val players: MutableList<String> = mutableListOf()
            for(player in scores) {
                players.add(player.first.toString())
            }

            val playerScores: MutableList<Int> = mutableListOf()
            for(score in scores) {
                playerScores.add(score.second)
            }

            val intent = Intent(baseContext, ScorePopup::class.java).apply {
                putExtra(SCORES, playerScores.toIntArray())
                putExtra(PLAYERS, players.toTypedArray())
            }
            startActivity(intent)
        }
    }
}