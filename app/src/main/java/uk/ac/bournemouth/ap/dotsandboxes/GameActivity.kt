package uk.ac.bournemouth.ap.dotsandboxes

import android.app.ActionBar
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player


class GameActivity: AppCompatActivity() {


    // Listen for game over. Start popup.
    private val gameOverListener = object: DotsAndBoxesGame.GameOverListener {
        override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
            showScores(scores)
            //Toast.makeText(baseContext, "END", Toast.LENGTH_SHORT).show()

        }
    }


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



    fun showScores(scores: List<Pair<Player, Int>>) {
        /*
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.activity_popup, null)
        val density = resources.displayMetrics.density
        val popUp = PopupWindow(layout, (density*240).toInt(), (density*240).toInt(), true)
        popUp.showAtLocation(layout, Gravity.CENTER, 0, 0)
        */
        /*
        layout.findViewById<Button>(R.id.popup_close).setOnClickListener {
            fun onClick(view: View) {
            // dismiss popup.
            popUp.dismiss()
        }}
        */
    }
}