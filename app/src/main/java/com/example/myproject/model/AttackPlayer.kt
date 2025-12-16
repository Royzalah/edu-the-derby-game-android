package com.example.myproject.model

class AttackPlayer(
    private val numLanes: Int
) {

    var currentLane: Int = 1
        private set

    fun move(direction: Int) {
        val newLane = currentLane + direction

        if (newLane in 0 until numLanes) {
            currentLane = newLane
        } else if (newLane < 0) {
            currentLane = numLanes - 1
        } else {
            currentLane = 0
        }
    }
}