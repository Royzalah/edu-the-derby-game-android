package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.fragments.HighScoreFragment
import com.example.myproject.fragments.MapFragment
import com.example.myproject.interfaces.CallbackHighScore
import com.example.myproject.utilities.SharedPreferencesManager
import com.example.myproject.utilities.SignalManager
import com.google.android.material.button.MaterialButton

class HighScoreActivity : AppCompatActivity() {

    private lateinit var main_FRAME_list: FrameLayout
    private lateinit var main_FRAME_map: FrameLayout
    private lateinit var highScore_BTN_play_again: MaterialButton
    private lateinit var highScore_BTN_menu: MaterialButton

    private lateinit var mapFragment: MapFragment
    private lateinit var highScoreFragment: HighScoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.high_score_activity)

        SignalManager.init(this)
        SharedPreferencesManager.init(this)
        SignalManager.getInstance().playMusic(R.raw.sound_chicago_intro)

        findViews()
        initViews()
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
        highScore_BTN_play_again = findViewById(R.id.high_score_BTN_play_again)
        highScore_BTN_menu = findViewById(R.id.high_score_BTN_menu)
    }

    private fun initViews() {
        val allScores = SharedPreferencesManager.getInstance().getAllScores()

        mapFragment = MapFragment()
        mapFragment.updateScores(allScores)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_FRAME_map, mapFragment)
            .commit()

        highScoreFragment = HighScoreFragment()

        // Setup Callback: Clicking a score item zooms the Map Fragment
        highScoreFragment.callback = object : CallbackHighScore {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                mapFragment.zoom(lat, lon)
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_FRAME_list, highScoreFragment)
            .commit()

        highScore_BTN_play_again.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        highScore_BTN_menu.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        SignalManager.getInstance().stopMusic()
    }

    override fun onResume() {
        super.onResume()
        SignalManager.getInstance().resumeMusic()
    }
}