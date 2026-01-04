package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.logic.GameGridManager
import com.example.myproject.logic.GameManager
import com.example.myproject.model.AttackPlayer
import com.example.myproject.enum.FoulType
import com.example.myproject.utilities.Constants
import com.example.myproject.utilities.TiltDetector
import com.example.myproject.utilities.SignalManager
import com.example.myproject.utilities.VibrationManager
import com.example.myproject.logic.GameTicker

class GameActivity : AppCompatActivity(), TiltDetector.TiltCallback {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var right_BTN_arrow: AppCompatImageButton
    private lateinit var left_BTN_arrow: AppCompatImageButton
    private lateinit var foul_IMG_ref: AppCompatImageView
    private lateinit var linearMatrix: LinearLayout
    private lateinit var score_LBL: AppCompatTextView
    private lateinit var arrows_container: LinearLayout

    private lateinit var gameGridManager: GameGridManager
    private lateinit var attackPlayer: AttackPlayer
    private lateinit var gameManager: GameManager
    private lateinit var tiltDetector: TiltDetector

    // Replaced Handler/Runnable with GameTicker
    private lateinit var gameTicker: GameTicker

    private var baseSpeed: Long = Constants.GAME_TICK_MS
    private var useSensors: Boolean = false
    private var isFastMode: Boolean = false
    private var currentTiltY: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.game_activity)

        initBackgroundMusic()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        useSensors = intent.getBooleanExtra(Constants.KEY_SENSORS, false)
        isFastMode = intent.getBooleanExtra("KEY_FAST", false)

        findViews()

        // Set base speed
        baseSpeed = if (isFastMode) Constants.GAME_TICK_MS / 2 else Constants.GAME_TICK_MS

        // Initialize Ticker with the game loop logic
        gameTicker = GameTicker {
            updateGameStep()
        }

        gameGridManager = GameGridManager(linearMatrix)
        tiltDetector = TiltDetector(this, this)
        attackPlayer = AttackPlayer(Constants.NUM_LANES)
        gameManager = GameManager(
            Constants.NUM_ROWS,
            Constants.NUM_LANES,
            Constants.NUM_DEFENDERS,
            Constants.LIFE_COUNT
        )

        initViews()
        gameManager.initDefenders()
        gameGridManager.updateUI(gameManager.getDefenders(), attackPlayer.currentLane, gameManager.trophy)

        startGame()
    }

    // --- Main Logic extracted from Runnable ---
    private fun updateGameStep() {
        val hit = gameManager.updateGameLogic(attackPlayer.currentLane)

        gameGridManager.updateUI(
            gameManager.getDefenders(),
            attackPlayer.currentLane,
            gameManager.trophy
        )

        updateHearts()
        score_LBL.text = "${gameManager.score}"

        if (hit) {
            handleCollision()
        } else {
            if (gameManager.isGameOver) {
                goToResult()
            } else {
                updateSpeed()
            }
        }
    }

    private fun updateSpeed() {
        val baseDelay = if (gameManager.isSpeedActive) baseSpeed / 2 else baseSpeed

        val tiltEffect = when {
            currentTiltY < -Constants.TILT_THRESHOLD -> Constants.SPEED_MULTIPLIER_SLOW
            currentTiltY > Constants.TILT_THRESHOLD -> Constants.SPEED_MULTIPLIER_FAST
            else -> 1.0f
        }

        // Update the ticker interval dynamically
        gameTicker.interval = (baseDelay * tiltEffect).toLong()
    }

    private fun initBackgroundMusic() {
        val musicTracks = arrayOf(R.raw.sound_nba_game, R.raw.sound_nba_2)
        val randomTrack = musicTracks.random()
        SignalManager.getInstance().playMusic(randomTrack)
    }

    override fun onResume() {
        super.onResume()
        tiltDetector.start()
        SignalManager.getInstance().resumeMusic()
        if (!gameManager.isGameOver && !gameTicker.isRunning()) startGame()
    }

    override fun onPause() {
        super.onPause()
        if (useSensors) tiltDetector.stop()
        SignalManager.getInstance().stopMusic()
        stopGame()
    }

    override fun tiltX(x: Float) {
        if (useSensors) {
            if (x < -2.0) movePlayer(1)
            else if (x > 2.0) movePlayer(-1)
        }
    }

    override fun tiltY(y: Float) {
        currentTiltY = y
    }

    private fun handleCollision() {
        stopGame()
        val isGameOverAfterFoul = gameManager.handleFoulLogic()

        gameGridManager.updateUI(
            gameManager.getDefenders(),
            attackPlayer.currentLane,
            gameManager.trophy
        )

        updateHearts()
        SignalManager.getInstance().playSound(R.raw.sound_whistle)
        VibrationManager.getInstance().vibrate(500)

        showFoulForLives(gameManager.currentLives) {
            if (!isGameOverAfterFoul) startGame() else goToResult()
        }
    }

    private fun startGame() {
        gameTicker.start()
    }

    private fun stopGame() {
        gameTicker.stop()
    }

    private fun goToResult() {
        SignalManager.getInstance().playSound(R.raw.sound_buzzer)
        stopGame()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", gameManager.score)
        intent.putExtra(Constants.KEY_SENSORS, useSensors)
        intent.putExtra("KEY_FAST", isFastMode)
        startActivity(intent)
        finish()
    }

    private fun findViews() {
        linearMatrix = findViewById(R.id.linearMatrix)
        foul_IMG_ref = findViewById(R.id.foul_IMG_ref)
        score_LBL = findViewById(R.id.score_LBL)
        left_BTN_arrow = findViewById(R.id.left_BTN_arrow)
        right_BTN_arrow = findViewById(R.id.right_BTN_arrow)
        arrows_container = findViewById(R.id.arrows_container)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart0)
        )
    }

    private fun initViews() {
        if (useSensors) {
            arrows_container.visibility = View.INVISIBLE
        } else {
            arrows_container.visibility = View.VISIBLE
            right_BTN_arrow.setOnClickListener { movePlayer(1) }
            left_BTN_arrow.setOnClickListener { movePlayer(-1) }
        }
    }

    private fun updateHearts() {
        for (i in main_IMG_hearts.indices) {
            if (i < gameManager.currentLives) main_IMG_hearts[i].setImageResource(R.drawable.heart)
            else main_IMG_hearts[i].setImageResource(R.drawable.heart_empty)
        }
    }

    private fun showFoulForLives(lives: Int, onDone: () -> Unit) {
        val foulType = when (lives) {
            2 -> FoulType.FOUL1
            1 -> FoulType.FOUL2
            0 -> FoulType.FOUL3
            else -> null
        }
        foulType?.let {
            foul_IMG_ref.setImageResource(it.drawableRes)
            foul_IMG_ref.visibility = View.VISIBLE
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                foul_IMG_ref.visibility = View.GONE
                onDone()
            }, Constants.FOUL_DELAY_MS)
        }
    }

    private fun movePlayer(direction: Int) {
        attackPlayer.move(direction)
        gameGridManager.updateUI(
            gameManager.getDefenders(),
            attackPlayer.currentLane,
            gameManager.trophy
        )
    }
}