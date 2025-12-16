package com.example.myproject.model


class Defender(
    var row: Int,
    var col: Int,
    var type: DefenderType = DefenderType.getRandom()

) {
    fun moveDown(maxRows: Int): Boolean {
        row++
        return row >= maxRows
    }

    fun reset(numLanes: Int) {
        row = 0
        col = (0 until numLanes).random()
        type = DefenderType.getRandom()
    }
}