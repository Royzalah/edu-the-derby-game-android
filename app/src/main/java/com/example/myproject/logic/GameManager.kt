package com.example.myproject.logic

import com.example.myproject.model.Defender
import com.example.myproject.model.DefenderType
// Removed Android/UI imports (View, AppCompatImageView)

class GameManager(
    private val numRows: Int,
    private val numLanes: Int,
    private val numDefenders: Int,
    lifeCount: Int
) {

    private val defenders = Array(numDefenders) {
        Defender(row = -1, col = (0 until numLanes).random())
    }

    private val defenderDelay = IntArray(numDefenders)

    var currentLives: Int = lifeCount
        private set

    val isGameOver: Boolean
        get() = currentLives <= 0

    // 1. PURE LOGIC: Initialize defenders' logical positions
    fun initDefenders() {
        // NOTE: This function only deals with resetting internal data (defenders array)
        for (i in 0 until numDefenders) {
            defenders[i].row = -1
            defenders[i].col = (0 until numLanes).random()
            defenders[i].type = DefenderType.getRandom()
            defenderDelay[i] = (i * 2) + 1
        }

    }

    // 2. PURE LOGIC: Update positions and check for collision
    fun updateDefenders(playerLane: Int): Boolean {
        var hit = false

        for (i in 0 until numDefenders) {
            if (defenderDelay[i] > 0) {
                defenderDelay[i]--
                continue
            }

            val defender = defenders[i]

            val reachedBottom = defender.moveDown(numRows)

            if (reachedBottom) {
                defender.reset(numLanes)
            }


            if (defender.row == numRows - 1 && defender.col == playerLane) {
                hit = true
            }
        }

        return hit
    }

    // 3. Handle life reduction and game over status
    fun handleFoulLogic(): Boolean {
        currentLives--
        return isGameOver
    }

    // 4. DATA ACCESS: Expose logical defender data for the Activity to updateUI
    fun getDefenders(): Array<Defender> {
        // IMPORTANT: GameActivity uses this method to get the necessary data for drawing
        return defenders
    }
}