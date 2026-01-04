package com.example.myproject.logic

import com.example.myproject.model.Defender
import com.example.myproject.enum.DefenderType
import com.example.myproject.model.Trophy
import com.example.myproject.enum.TrophyType
import com.example.myproject.utilities.Constants
import com.example.myproject.utilities.SignalManager

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
    val trophy = Trophy(row = -1, col = 0)
    var score: Int = 0
        private set
    var distance: Int = 0
        private set

    // Timers for active power-ups
    private var shieldEndTime: Long = 0
    private var speedEndTime: Long = 0

    val isGameOver: Boolean
        get() = currentLives <= 0

    val isShieldActive: Boolean
        get() = System.currentTimeMillis() < shieldEndTime

    val isSpeedActive: Boolean
        get() = System.currentTimeMillis() < speedEndTime

    // Initialize defenders with staggered start times (delay)
    fun initDefenders() {
        for (i in 0 until numDefenders) {
            defenders[i].row = -1
            defenders[i].col = (0 until numLanes).random()
            defenders[i].type = DefenderType.getRandom()
            defenderDelay[i] = (i * 2) + 1
        }
    }

    // Move defenders down and check for collisions with player
    private fun updateDefenders(playerLane: Int): Boolean {
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

            // Collision detection logic
            if (defender.row == numRows - 1 && defender.col == playerLane) {
                if (!isShieldActive) {
                    hit = true
                }
            }
        }
        return hit
    }

    // Main game loop tick: updates positions, scores, and returns collision status
    fun updateGameLogic(playerLane: Int): Boolean {
        val hitDefender = updateDefenders(playerLane)
        val collectedTrophy = updateTrophy(playerLane)


        distance += Constants.DISTANCE_PER_TICK
        score += Constants.SCORE_REGULAR

        // If trophy collected, ignore collision in this tick (optional design choice)
        if (collectedTrophy) {
            return false
        }
        return hitDefender
    }

    // Handles trophy spawning, movement, and collection
    private fun updateTrophy(playerLane: Int): Boolean {
        var collected = false
        val randomChance = (0..1).random()

        // Try to spawn new trophy if none exists
        if (!trophy.isActive) {
            if (randomChance == 1) {
                trophy.spawn(numLanes)
            }
        } else {
            val reachedBottom = trophy.moveTrophy(numRows)
            // Check if player caught the trophy
            if (trophy.row == numRows - 1 && trophy.col == playerLane) {
                applyTrophyEffect(trophy.type)
                trophy.remove()
                collected = true
            } else if (reachedBottom) {
                trophy.remove()
            }
        }
        return collected
    }

    // Applies power-up effects (Heal, Shield, Speed)
    private fun applyTrophyEffect(type: TrophyType) {
        score += type.scoreValue

        SignalManager.getInstance().playSound(type.soundRes)

        when {
            type.isHeal -> {
                if (currentLives < Constants.LIFE_COUNT) {
                    currentLives++
                }
            }
            type.isShield -> {
                shieldEndTime = System.currentTimeMillis() + type.effectDuration
            }
            type.isSpeedBoost -> {
                speedEndTime = System.currentTimeMillis() + type.effectDuration
            }
        }
    }

    fun handleFoulLogic(): Boolean {
        currentLives--
        return isGameOver
    }

    fun getDefenders(): Array<Defender> {
        return defenders
    }
}