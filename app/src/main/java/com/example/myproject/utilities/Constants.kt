package com.example.myproject.utilities

// Global Game Configuration
object Constants {
    const val NUM_ROWS = 7      // Number of rows in the game grid
    const val NUM_LANES = 5      // Number of columns

    const val NUM_DEFENDERS = 6  // Total number of active obstacles

    const val GAME_TICK_MS = 800L // Base game speed (tick rate)

    const val TILT_THRESHOLD = 2.0f
    const val SPEED_MULTIPLIER_FAST = 0.6f
    const val SPEED_MULTIPLIER_SLOW = 1.6f
    const val DISTANCE_PER_TICK = 10

    const val LIFE_COUNT = 3

    const val DURATION_SHIELD = 4000L
    const val DURATION_SPEED = 3000L

    const val SCORE_BONUS = 50
    const val SCORE_REGULAR = 10
    const val KEY_SENSORS = "KEY_SENSORS"
    const val FOUL_DELAY_MS = 2000L // Duration to display foul animation
}