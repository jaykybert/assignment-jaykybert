package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

// Intent Keys
const val HUMANS = "uk.ac.bournemouth.ap.dotsandboxes.HUMANS"
const val BOTS = "uk.ac.bournemouth.ap.dotsandboxes.BOTS"
const val DIFFICULTY = "uk.ac.bournemouth.ap.dotsandboxes.DIFFICULTY"
const val COLUMNS = "uk.ac.bournemouth.ap.dotsandboxes.COLUMNS"
const val ROWS = "uk.ac.bournemouth.ap.dotsandboxes.ROWS"
const val SHUFFLE = "uk.ac.bournemouth.ap.dotsandboxes.SHUFFLE"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SeekBars - set default and max values.
        humanPlayers.max = 5
        humanPlayers.progress = 1
        humanPlayersText.text = getString(R.string.slider_human_text, 1)

        computerPlayers.max = 5
        computerPlayers.progress = 1
        computerPlayersText.text = getString(R.string.slider_computer_text, 1)

        computerDifficulty.max = 2
        computerDifficulty.progress = 0
        computerDifficultyText.text = getString(R.string.slider_difficulty_text, 1)

        columns.max = 14
        columns.progress = 2
        columnsText.text = getString(R.string.slider_column_text, 3)

        rows.max = 14
        rows.progress = 2
        rowsText.text = getString(R.string.slider_row_text, 3)

        shuffleText.text = getString(R.string.switch_shuffle_text, "no")


        // SeekBar Listeners - update text when changed.
        humanPlayers.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                humanPlayersText.text = getString(R.string.slider_human_text, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        computerPlayers.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                computerPlayersText.text = getString(R.string.slider_computer_text, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        computerDifficulty.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                computerDifficultyText.text = getString(R.string.slider_difficulty_text, progress+1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        columns.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                columnsText.text = getString(R.string.slider_column_text, progress+1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        rows.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rowsText.text = getString(R.string.slider_row_text, progress+1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        // Set shuffle to whether the switch is checked.
        shufflePlayers.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                shuffleText.text = getString(R.string.switch_shuffle_text, "yes")
            } else {
                shuffleText.text = getString(R.string.switch_shuffle_text, "no")
            }
        }
    }


    /** Called by [startGame] button.
     *  Launches the [GameActivity] class with the data from the SeekBars.
     */
    fun startGame(view: View) {
        if(humanPlayers.progress == 0 && computerPlayers.progress == 0) {
            Toast.makeText(this, "Please select the number of players.", Toast.LENGTH_SHORT).show()
        }

        else {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(HUMANS, humanPlayers.progress)
                putExtra(BOTS, computerPlayers.progress)
                putExtra(DIFFICULTY, computerDifficulty.progress + 1)
                putExtra(COLUMNS, columns.progress + 1)
                putExtra(ROWS, rows.progress + 1)
                putExtra(SHUFFLE, shufflePlayers.isChecked)
            }
            startActivity(intent)
        }
    }
}
