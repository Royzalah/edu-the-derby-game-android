package com.example.myproject.utilities


import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class VibrationManager private constructor(context: Context) {
    private val vibrator: Vibrator

    init {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun vibrate(duration: Long) {
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

    companion object {
        @Volatile
        private var instance: VibrationManager? = null

        fun init(context: Context): VibrationManager {
            return instance ?: synchronized(this) {
                instance ?: VibrationManager(context).also { instance = it }
            }
        }

        fun getInstance(): VibrationManager {
            return instance ?: throw IllegalStateException("VibrationManager not initialized")
        }
    }
}