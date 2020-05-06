package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*


// Intent Keys
const val HUMANS = "uk.ac.bournemouth.ap.dotsandboxes.HUMANS"
const val BOTS = "uk.ac.bournemouth.ap.dotsandboxes.BOTS"
const val DIFFICULTY = "uk.ac.bournemouth.ap.dotsandboxes.DIFFICULTY"
const val COLUMNS = "uk.ac.bournemouth.ap.dotsandboxes.COLUMNS"
const val ROWS = "uk.ac.bournemouth.ap.dotsandboxes.ROWS"
const val SHUFFLE = "uk.ac.bournemouth.ap.dotsandboxes.SHUFFLE"


/** The Main Menu.
 *  Allows the user to change various game settings and launch the game.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the minimum, maximum and default values of the SeekBars (and related text).
        val getHumans = findViewById<SeekBar>(R.id.humanPlayers)
        getHumans.max = 5
        getHumans.progress = 1
        val getHumansText = findViewById<TextView>(R.id.humanPlayersText)
        getHumansText.text = getString(R.string.slider_human_text, 1)

        val getBots = findViewById<SeekBar>(R.id.computerPlayers)
        getBots.max = 5
        getBots.progress = 1
        val getBotsText = findViewById<TextView>(R.id.computerPlayersText)
        getBotsText.text = getString(R.string.slider_computer_text, 1)

        val getDifficulty = findViewById<SeekBar>(R.id.computerDifficulty)
        getDifficulty.max = 2
        getDifficulty.progress = 0
        val getDifficultyText = findViewById<TextView>(R.id.computerDifficultyText)
        getDifficultyText.text = getString(R.string.slider_difficulty_text, 1)

        val getColumns = findViewById<SeekBar>(R.id.columns)
        getColumns.max = 14
        getColumns.progress = 4
        val getColumnsText = findViewById<TextView>(R.id.columnsText)
        getColumnsText.text = getString(R.string.slider_column_text, 5)

        val getRows = findViewById<SeekBar>(R.id.rows)
        getRows.max = 14
        getRows.progress = 4
        val getRowsText = findViewById<TextView>(R.id.rowsText)
        getRowsText.text = getString(R.string.slider_row_text, 5)

        val getShuffle = findViewById<Switch>(R.id.shufflePlayers)
        val getShuffleText = findViewById<TextView>(R.id.shuffleText)
        getShuffleText.text = getString(R.string.switch_shuffle_text, "no")

        // SeekBar Listeners - update the related TextView when changed.
        getHumans.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getHumansText.text = getString(R.string.slider_human_text, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        getBots.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getBotsText.text = getString(R.string.slider_computer_text, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        getDifficulty.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getDifficultyText.text = getString(R.string.slider_difficulty_text, progress+1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        getColumns.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getColumnsText.text = getString(R.string.slider_column_text, progress+1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        getRows.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getRowsText.text = getString(R.string.slider_row_text, progress+1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        // Set shuffle to whether the switch is checked.
        getShuffle.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                getShuffleText.text = getString(R.string.switch_shuffle_text, "yes")
            }
            else {
                getShuffleText.text = getString(R.string.switch_shuffle_text, "no")
            }
        }
    }


    /** Called by [startGame] button.
     *  Launches the [GameActivity] class with the data from the SeekBars.
     */
    fun startGame(view: View) {
        if(findViewById<SeekBar>(R.id.humanPlayers).progress == 0 &&
                findViewById<SeekBar>(R.id.computerPlayers).progress == 0) {
            Toast.makeText(this, "Please specify the number of players.", Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(HUMANS, findViewById<SeekBar>(R.id.humanPlayers).progress)
                putExtra(BOTS, findViewById<SeekBar>(R.id.computerPlayers).progress)
                putExtra(DIFFICULTY, findViewById<SeekBar>(R.id.computerDifficulty).progress + 1)
                putExtra(COLUMNS, findViewById<SeekBar>(R.id.columns).progress + 1)
                putExtra(ROWS, findViewById<SeekBar>(R.id.rows).progress + 1)
                putExtra(SHUFFLE, findViewById<Switch>(R.id.shufflePlayers).isChecked)
            }
            startActivity(intent)
        }
    }
}
