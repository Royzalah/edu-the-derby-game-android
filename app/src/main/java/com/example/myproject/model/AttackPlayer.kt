package com.example.myproject.model

class AttackPlayer(
    private val numLanes: Int
) {

    var currentLane: Int = 1
        private set

    // Updates player lane with boundary checks (0 to numLanes)
    fun move(direction: Int) {
        val newLane = currentLane + direction
        if (newLane in 0 until numLanes) {
            currentLane = newLane
        }

    }
}