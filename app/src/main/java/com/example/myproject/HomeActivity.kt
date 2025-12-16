package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Find the "Start Game" button/label
        val startGameBtn = findViewById<MaterialButton>(R.id.start_game_BTN)
        // Set the click listener to start the game
        startGameBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish() // Close the HomeActivity so the user cannot navigate back to it
        }
    }
}