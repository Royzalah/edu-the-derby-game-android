package com.example.myproject.interfaces

// Interface to communicate clicks from the RecyclerView to the parent Activity/Fragment
interface CallbackHighScore {
    fun highScoreItemClicked(lat: Double, lon: Double)
}