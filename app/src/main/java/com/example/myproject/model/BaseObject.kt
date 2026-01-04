package com.example.myproject.model

// Base class for any moving object on the grid (Defenders, Trophies)
open class BaseObject (
    var row: Int,
    var col: Int
){
    // Moves object down one row. Returns TRUE if it reached the bottom (out of bounds).
    fun moveDown(maxRows: Int): Boolean {
        row++
        return row >= maxRows
    }
}