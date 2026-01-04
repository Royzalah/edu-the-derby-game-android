package com.example.myproject.model

import com.example.myproject.enum.DefenderType

// Represents an enemy object; Inherits movement logic from BaseObject
class Defender(row: Int, col: Int) : BaseObject(row, col) {
    var type = DefenderType.entries.random()

    // Resets the defender to the top with a new random lane and skin
    fun reset(numLanes: Int) {
        row = 0
        col = (0 until numLanes).random()
        type = DefenderType.values().random()
    }
}