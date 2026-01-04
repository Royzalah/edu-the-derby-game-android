package com.example.myproject.utilities

// Data model for High Scores including location data
data class Score(
    val name: String = "",
    val score: Int = 0,
    val lat: Double = 0.0,
    val lon: Double = 0.0
)