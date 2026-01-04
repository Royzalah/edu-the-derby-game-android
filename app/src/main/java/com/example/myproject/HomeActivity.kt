package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.example.myproject.utilities.Constants
import com.example.myproject.utilities.SignalManager

class HomeActivity : AppCompatActivity() {

    private lateinit var btnSensors: MaterialButton
    private lateinit var btnArrows: MaterialButton
    private lateinit var btnSlow: MaterialButton
    private lateinit var btnFast: MaterialButton
    private lateinit var btnStart: MaterialButton
    private lateinit var btnTop10: MaterialButton

    private var isSensorsMode = false
    private var isFastMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Singleton Managers
        com.example.myproject.utilities.SharedPreferencesManager.init(this)
        com.example.myproject.utilities.SignalManager.init(this)

        setContentView(R.layout.home_activity)
        com.example.myproject.utilities.SignalManager.getInstance().playMusic(R.raw.sound_chicago_intro)
        findViews()
        initViews()
    }

    private fun findViews() {
        btnSensors = findViewById(R.id.mode_BTN_tilt)
        btnArrows = findViewById(R.id.mode_BTN_arrows)
        btnSlow = findViewById(R.id.level_BTN_slow)
        btnFast = findViewById(R.id.level_BTN_fast)
        btnStart = findViewById(R.id.start_BTN)
        btnTop10 = findViewById(R.id.top10_BTN)

    }


    override fun onPause() {
        super.onPause()
        SignalManager.getInstance().stopMusic()
    }

    override fun onResume() {
        super.onResume()
        SignalManager.getInstance().resumeMusic()
    }

    private fun initViews() {
        updateUI()

        // Toggle Logic for Game Modes
        btnSensors.setOnClickListener {
            isSensorsMode = true
            updateUI()
        }

        btnArrows.setOnClickListener {
            isSensorsMode = false
            updateUI()
        }

        btnSlow.setOnClickListener {
            isFastMode = false
            updateUI()
        }

        btnFast.setOnClickListener {
            isFastMode = true
            updateUI()
        }

        btnStart.setOnClickListener { view ->
            animateButtonAndStart(view)
        }

        btnTop10.setOnClickListener {
            showTop10()
        }
    }

    private fun animateButtonAndStart(view: View) {
        view.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(120)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(120)
                    .withEndAction {
                        startGame()
                    }
                    .start()
            }
            .start()
    }

    // Updates visual state of buttons (Selected vs Unselected)
    private fun updateUI() {
        if (isSensorsMode) {
            setButtonSelected(btnSensors, true)
            setButtonSelected(btnArrows, false)
        } else {
            setButtonSelected(btnSensors, false)
            setButtonSelected(btnArrows, true)
        }

        if (isFastMode) {
            setButtonSelected(btnFast, true)
            setButtonSelected(btnSlow, false)
        } else {
            setButtonSelected(btnFast, false)
            setButtonSelected(btnSlow, true)
        }
    }

    private fun setButtonSelected(btn: MaterialButton, isSelected: Boolean) {
        if (isSelected) {
            btn.strokeWidth = 6
            btn.setStrokeColorResource(android.R.color.holo_orange_light)
            btn.alpha = 1.0f
        } else {
            btn.strokeWidth = 2
            btn.setStrokeColorResource(android.R.color.darker_gray)
            btn.alpha = 0.5f
        }
    }

    // Launches game with selected configuration
    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(Constants.KEY_SENSORS, isSensorsMode)
        intent.putExtra("KEY_FAST", isFastMode)
        com.example.myproject.utilities.SignalManager.getInstance().playMusic(R.raw.sound_nba_game)
        startActivity(intent)
        finish()
    }

    private fun showTop10() {
        val intent = Intent(this, HighScoreActivity::class.java)
        startActivity(intent)
        finish()
    }
}