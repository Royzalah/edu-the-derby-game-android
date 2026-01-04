package com.example.myproject.logic

import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.example.myproject.R
import com.example.myproject.model.Defender
import com.example.myproject.model.Trophy
import com.example.myproject.utilities.Constants

class GameGridManager(private val linearMatrix: LinearLayout) {

    // Maps the UI Layout hierarchy into a 2D Array for easier logic manipulation
    private val gameGrid: Array<Array<AppCompatImageView>> = Array(Constants.NUM_ROWS) { row ->
        val rowLayout = linearMatrix.getChildAt(row) as LinearLayout
        Array(Constants.NUM_LANES) { col ->
            rowLayout.getChildAt(col) as AppCompatImageView
        }
    }

    // Refreshes the grid: Clears previous state -> Draws Player -> Draws Defenders -> Draws Trophies
    fun updateUI(defenders: Array<Defender>, playerLane: Int, trophy: Trophy) {
        for (row in 0 until Constants.NUM_ROWS) {
            for (col in 0 until Constants.NUM_LANES) {
                gameGrid[row][col].visibility = View.INVISIBLE
                gameGrid[row][col].setImageDrawable(null)
            }
        }


        val playerCell = gameGrid[Constants.NUM_ROWS - 1][playerLane]
        playerCell.setImageResource(R.drawable.attack_player)
        playerCell.visibility = View.VISIBLE


        for (defender in defenders) {
            if (defender.row in 0 until Constants.NUM_ROWS - 1) {
                gameGrid[defender.row][defender.col].setImageResource(defender.type.drawableResources)
                gameGrid[defender.row][defender.col].visibility = View.VISIBLE
            }
        }


        if (trophy.isActive && trophy.row < Constants.NUM_ROWS - 1 && trophy.row >= 0) {
            gameGrid[trophy.row][trophy.col].setImageResource(trophy.type.assetImage)
            gameGrid[trophy.row][trophy.col].visibility = View.VISIBLE
        }
    }
}