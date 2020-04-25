package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

const val HUMANS = "uk.ac.bournemouth.ap.dotsandboxes.HUMANS"
const val BOTS = "uk.ac.bournemouth.ap.dotsandboxes.BOTS"
const val COLUMNS = "uk.ac.bournemouth.ap.dotsandboxes.COLUMNS"
const val ROWS = "uk.ac.bournemouth.ap.dotsandboxes.ROWS"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playersDefault = 1 // Default value on SeekBar and initial value in text views.
        val gameGridDefault = 3

        // SeekBars - Input
        val humanPlayers = findViewById<SeekBar>(R.id.humanPlayers)
        val humanPlayersText = findViewById<TextView>(R.id.humanPlayersText)
        humanPlayers.max = 5
        humanPlayers.progress = playersDefault
        humanPlayersText.text = getString(R.string.slider_human_text, playersDefault)

        val computerPlayers = findViewById<SeekBar>(R.id.computerPlayers)
        val computerPlayersText = findViewById<TextView>(R.id.computerPlayersText)
        computerPlayers.max = 5
        computerPlayers.progress = playersDefault
        computerPlayersText.text = getString(R.string.slider_computer_text, playersDefault)

        val columns = findViewById<SeekBar>(R.id.columns)
        val columnsText = findViewById<TextView>(R.id.columnsText)
        columns.max = 10
        columns.progress = gameGridDefault
        columnsText.text = getString(R.string.slider_column_text, gameGridDefault)

        val rows = findViewById<SeekBar>(R.id.rows)
        val rowsText = findViewById<TextView>(R.id.rowsText)
        rows.max = 10
        rows.progress = gameGridDefault
        rowsText.text = getString(R.string.slider_row_text, gameGridDefault)


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

        columns.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                columnsText.text = getString(R.string.slider_column_text, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        rows.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rowsText.text = getString(R.string.slider_row_text, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })
    }


    fun startGame(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(HUMANS, humanPlayers.progress)
            putExtra(BOTS, computerPlayers.progress)
            putExtra(COLUMNS, columns.progress)
            putExtra(ROWS, rows.progress)

        }
        startActivity(intent)
    }
}
