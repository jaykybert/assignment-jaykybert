package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_bar, menu)
        return true
    }

    /** Toolbar/menu.
     * [R.id.restartGame] refreshes the game with the current settings.
     * The arrow returns the user to [MainActivity].
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if(item.itemId == R.id.restartGame) {
            finish()
            this.startActivity(intent) }
        else {
            finish()
            startActivity(Intent(this, MainActivity::class.java)) }
        return true
    }


    /**
     * Launch [GameView] with the parameters from [MainActivity].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val humanPlayers = intent.getIntExtra(HUMANS, 1)
        val computerPlayers = intent.getIntExtra(BOTS, 1)
        val computerDifficulty = intent.getIntExtra(DIFFICULTY, 1)
        val columns = intent.getIntExtra(COLUMNS, 5)
        val rows = intent.getIntExtra(ROWS, 5)
        val shuffle = intent.getBooleanExtra(SHUFFLE, false)
        val gameView = GameView(columns, rows, humanPlayers, computerPlayers, computerDifficulty, shuffle, this)
        setContentView(gameView)
    }
}
