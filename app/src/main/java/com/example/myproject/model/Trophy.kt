package com.example.myproject.model

import com.example.myproject.enum.TrophyType

class Trophy(row: Int, col: Int) : BaseObject(row, col) {

    var type = TrophyType.entries.random()
    var isActive: Boolean = false

    // Activates the trophy and places it at the top of a random lane
    fun spawn(numLanes: Int) {
        row = 0
        col = (0 until numLanes).random()

        type = com.example.myproject.enum.TrophyType.values().random()
        isActive = true
    }

    fun remove() {
        isActive = false
        row = -1
    }

    fun moveTrophy(maxRows: Int): Boolean {
        if (isActive) {
            return super.moveDown(maxRows)
        }
        return false
    }
}