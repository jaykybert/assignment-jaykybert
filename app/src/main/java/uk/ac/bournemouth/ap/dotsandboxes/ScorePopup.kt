package uk.ac.bournemouth.ap.dotsandboxes

import android.app.Activity
import android.graphics.Canvas
import android.os.Bundle
import android.util.DisplayMetrics
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class ScorePopup : Activity() {

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_popup)

         val dm = DisplayMetrics()
         windowManager.defaultDisplay.getMetrics(dm)

         val popupWidth: Int = (dm.widthPixels * 0.6).toInt()
         val popupHeight: Int = (dm.heightPixels * 0.6).toInt()

         window.setLayout(popupWidth, popupHeight)
    }




}