package uk.ac.bournemouth.ap.dotsandboxes

import android.app.ActionBar
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get game parameters from MainActivity.
        val humanPlayers = intent.getIntExtra(HUMANS, 1)
        val computerPlayers = intent.getIntExtra(BOTS, 1)
        val columns = intent.getIntExtra(COLUMNS, 3)
        val rows = intent.getIntExtra(ROWS, 3)
        val gameView = GameView(columns, rows, humanPlayers, computerPlayers, this)
        setContentView(gameView)
    }
}