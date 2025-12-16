package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        findView()
    }

    // Finds UI elements and sets up click listeners
    private fun findView(){
        val btnRestart: Button = findViewById(R.id.restart_game_BTN)
        btnRestart.setOnClickListener {
            startGame()
        }
        val btnHome: Button = findViewById(R.id.home_BTN)
        btnHome.setOnClickListener {
            goHome()
        }
    }

    // Starts a new GameActivity instance
    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish() // Close ResultActivity
    }

    // Navigates back to the main menu/HomeActivity
    private fun goHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Close ResultActivity
    }

}