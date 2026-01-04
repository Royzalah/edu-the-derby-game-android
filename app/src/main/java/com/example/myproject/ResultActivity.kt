package com.example.myproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myproject.utilities.Constants
import com.example.myproject.utilities.Score
import com.example.myproject.utilities.SharedPreferencesManager
import com.example.myproject.utilities.SignalManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ResultActivity : AppCompatActivity() {

    private lateinit var score_LBL: TextView
    private lateinit var name_ET: EditText
    private lateinit var save_BTN: Button
    private lateinit var restart_game_BTN: Button
    private lateinit var home_BTN: Button
    private lateinit var score_BTN: Button

    private var score: Int = 0

    private var useSensors: Boolean = false
    private var isFastMode: Boolean = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)

        SignalManager.init(this)
        SharedPreferencesManager.init(this)

        SignalManager.getInstance().playMusic(R.raw.sound_chicago_intro)

        score = intent.getIntExtra("SCORE", 0)


        useSensors = intent.getBooleanExtra(Constants.KEY_SENSORS, false)
        isFastMode = intent.getBooleanExtra("KEY_FAST", false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViews()
        initViews()
    }

    private fun findViews() {
        score_LBL = findViewById(R.id.score_LBL)
        name_ET = findViewById(R.id.name_ET)
        save_BTN = findViewById(R.id.save_BTN)
        restart_game_BTN = findViewById(R.id.restart_game_BTN)
        home_BTN = findViewById(R.id.home_BTN)
        score_BTN = findViewById(R.id.score_BTN)
    }

    private fun initViews() {
        score_LBL.text = "Score: $score"

        save_BTN.setOnClickListener {
            checkLocationPermissionAndSave()
        }

        restart_game_BTN.setOnClickListener {
            startGame()
        }

        home_BTN.setOnClickListener {
            goHome()
        }

        score_BTN.setOnClickListener {
            goToHighScores()
        }
    }

    // Requests Runtime Permissions for location if not already granted
    private fun checkLocationPermissionAndSave() {
        val name = name_ET.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLastLocationAndSave(name)
        }
    }

    // Fetches last known location asynchronously and saves score
    private fun getLastLocationAndSave(name: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val lat = location?.latitude ?: 0.0
                    val lon = location?.longitude ?: 0.0
                    saveScoreToSP(name, lat, lon)
                }
                .addOnFailureListener {
                    saveScoreToSP(name, 0.0, 0.0)
                }
        } else {
            // Permission denied - save with default location
            saveScoreToSP(name, 0.0, 0.0)
        }
    }

    private fun saveScoreToSP(name: String, lat: Double, lon: Double) {
        val newScore = Score(
            name = name,
            score = score,
            lat = lat,
            lon = lon
        )
        SharedPreferencesManager.getInstance().saveScore(newScore)
        goToHighScores()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            val name = name_ET.text.toString()
            if (name.isNotEmpty()) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocationAndSave(name)
                } else {
                    saveScoreToSP(name, 0.0, 0.0)
                }
            }
        }
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)

        intent.putExtra(Constants.KEY_SENSORS, useSensors)
        intent.putExtra("KEY_FAST", isFastMode)

        startActivity(intent)
        finish()
    }

    private fun goHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHighScores() {
        val intent = Intent(this, HighScoreActivity::class.java)
        startActivity(intent)
        finish()
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