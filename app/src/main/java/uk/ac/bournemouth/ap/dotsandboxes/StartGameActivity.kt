package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player


const val PLAYERS = "uk.ac.bournemouth.ap.dotsandboxes.PLAYERS"
const val SCORES = "uk.ac.bournemouth.ap.dotsandboxes.SCORES"


class StartGameActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameView = GameView(2, 2, this)
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