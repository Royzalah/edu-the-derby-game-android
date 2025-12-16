package com.example.myproject

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.logic.GameManager
import com.example.myproject.model.AttackPlayer
import com.example.myproject.model.FoulType
import com.example.myproject.utilites.Constants

class GameActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var right_BTN_arrow: ImageButton
    private lateinit var left_BTN_arrow: ImageButton
    private lateinit var foul_IMG_ref: AppCompatImageView
    private lateinit var linearMatrix: LinearLayout

    private lateinit var gameGride: Array<Array<AppCompatImageView>>
    private lateinit var attackPlayer: AttackPlayer
    private lateinit var gameManager: GameManager

    private val handler = Handler(Looper.getMainLooper())
    private val gameTickMs = Constants.GAME_TICK_MS
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.game_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        buildGameGrid()

        // 1. Initialize AttackPlayer
        attackPlayer = AttackPlayer(
            numLanes = Constants.NUM_LANES
        )


        // 2. Initialize GameManager (Pure Logic)
        gameManager = GameManager(
            numRows = Constants.NUM_ROWS,
            numLanes = Constants.NUM_LANES,
            numDefenders = Constants.NUM_DEFENDERS,
            // gameGride parameter removed
            lifeCount = Constants.LIFE_COUNT
        )

        initButtons()
        gameManager.initDefenders()
        updatePlayerUI() 

        startGame()
    }
    private val gameRunnable = object : Runnable {
        override fun run() {
            if (isFinishing || isDestroyed) return

            if (!isGameRunning) return

            val hit = gameManager.updateDefenders(attackPlayer.currentLane)

            updateDefendersUI()

            if (hit) {
                handleCollision()
            } else {
                if (!gameManager.isGameOver) {
                    if (!isFinishing && !isDestroyed) {
                        handler.postDelayed(this, gameTickMs)
                    }
                } else {
                    goToResult()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!gameManager.isGameOver && !isGameRunning) {
            startGame()
        }
    }

    override fun onPause() {
        super.onPause()
        stopGame()
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


    private fun startGame() {
        if (!isGameRunning) {
            isGameRunning = true
            handler.post(gameRunnable)
        }
    }

    private fun stopGame() {
        isGameRunning = false
        handler.removeCallbacks(gameRunnable)
    }

    private fun goToResult() {
        stopGame()
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleCollision() {
        stopGame()

        //  life and check game over status
        val isGameOverAfterFoul = gameManager.handleFoulLogic()

        // update attacker in current position
        updatePlayerUI()

        foul_IMG_ref.visibility = View.VISIBLE
        updateHearts()

        val foulMessage = when (gameManager.currentLives) {
            2 -> "1st Foul"
            1 -> "2nd Foul"
            0 -> "You're Out!"
            else -> "Foul!"
        }
        Toast.makeText(this, foulMessage, Toast.LENGTH_SHORT).show()
        vibrate()

        showFoulForLives(gameManager.currentLives) {
            foul_IMG_ref.visibility = View.GONE
            if (!isGameOverAfterFoul) { // Use the boolean result from handleFoulLogic
                startGame()
            } else {
                goToResult()
            }
        }
    }

    private fun findViews() {
        linearMatrix = findViewById(R.id.linearMatrix)
        left_BTN_arrow = findViewById(R.id.left_BTN_arrow)
        right_BTN_arrow = findViewById(R.id.right_BTN_arrow)
        foul_IMG_ref = findViewById(R.id.foul_IMG_ref)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart0)
        )
    }

    private fun initButtons() {
        right_BTN_arrow.setOnClickListener {
            if (::attackPlayer.isInitialized) {
                attackPlayer.move(1)
                updatePlayerUI()    // Draw player at new position
            }
        }
        left_BTN_arrow.setOnClickListener {
            if (::attackPlayer.isInitialized) {
                attackPlayer.move(-1)
                updatePlayerUI()    // Draw player at new position
            }
        }
    }

    private fun buildGameGrid() {
        gameGride = Array(Constants.NUM_ROWS) { row ->
            val rowLayout = linearMatrix.getChildAt(row) as LinearLayout
            Array(Constants.NUM_LANES) { col ->
                rowLayout.getChildAt(col) as AppCompatImageView
            }
        }

    }

    // update the Attacker
    private fun updatePlayerUI() {
        val bottomRow = gameGride[Constants.NUM_ROWS - 1]
        val currentLane = attackPlayer.currentLane

        // Clear the the bottom
        for (cell in bottomRow) {
            cell.setImageDrawable(null)
            cell.visibility = View.INVISIBLE
        }

        // Draw the player at the new position
        bottomRow[currentLane].setImageResource(R.drawable.attack_player)
        bottomRow[currentLane].visibility = View.VISIBLE
    }

    // update all Defenders
    private fun updateDefendersUI() {
        val defenders = gameManager.getDefenders() // Gets logical state

        // Clear the defender grid area (all but the bottom row)
        for (row in 0 until Constants.NUM_ROWS - 1) {
            for (col in 0 until Constants.NUM_LANES) {
                gameGride[row][col].visibility = View.INVISIBLE
                gameGride[row][col].setImageDrawable(null)
            }
        }

        for (defender in defenders) {
            val row = defender.row
            val col = defender.col

            if (row in 0 until Constants.NUM_ROWS - 1) { // Draw only if it's on the grid
                val img = gameGride[row][col]
                img.setImageResource(defender.type.drawableResources)
                img.visibility = View.VISIBLE
            }
        }
    }

    private fun updateHearts() {
        val currentLives = gameManager.currentLives

        for (i in main_IMG_hearts.indices) {
            val imageView = main_IMG_hearts[i]

            val lifeIndex =  i

            if (lifeIndex < currentLives) {
                imageView.setImageResource(R.drawable.heart)
                imageView.visibility = View.VISIBLE
            } else {
                imageView.setImageResource(R.drawable.heart_empty)
                imageView.visibility = View.VISIBLE
            }
        }
    }

    private fun showFoulForLives(lives: Int, onDone: () -> Unit) {
        val foulType = when (lives) {
            2 -> FoulType.FOUL1
            1 -> FoulType.FOUL2
            0 -> FoulType.FOUL3
            else -> null
        }

        foulType?.let { type ->
            foul_IMG_ref.setImageResource(type.drawableRes)
            foul_IMG_ref.visibility = View.VISIBLE

            handler.postDelayed({
                foul_IMG_ref.visibility = View.GONE
                onDone()
            }, Constants.FOUL_DELAY_MS)
        }
    }

    private fun vibrate(duration: Long = 500) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }
    }
}