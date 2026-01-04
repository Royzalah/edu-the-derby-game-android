package com.example.myproject.utilities

import android.content.Context
import android.media.MediaPlayer

// Singleton class handling all audio (Music & SFX)
class SignalManager private constructor(context: Context) {
    private val context: Context = context.applicationContext
    private var backgroundMusicPlayer: MediaPlayer? = null

    companion object {
        @Volatile
        private var instance: SignalManager? = null

        fun init(context: Context): SignalManager {
            return instance ?: synchronized(this) {
                instance ?: SignalManager(context).also { instance = it }
            }
        }

        fun getInstance(): SignalManager {
            return instance ?: throw IllegalStateException("SignalManager not initialized")
        }
    }

    fun playMusic(resourceId: Int) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer?.stop()
            backgroundMusicPlayer?.release()
        }

        backgroundMusicPlayer = MediaPlayer.create(context, resourceId)
        backgroundMusicPlayer?.isLooping = true
        backgroundMusicPlayer?.setVolume(0.8f, 0.8f)
        backgroundMusicPlayer?.start()
    }

    fun playSound(resourceId: Int) {
        try {
            // Ducking logic: Lower music volume while SFX plays
            backgroundMusicPlayer?.setVolume(0.10f, 0.10f)

            val effectPlayer = MediaPlayer.create(context, resourceId)
            effectPlayer.setVolume(0.9f, 0.9f)

            // Restore music volume when SFX finishes
            effectPlayer.setOnCompletionListener { mp ->
                mp.release()
                backgroundMusicPlayer?.setVolume(0.8f, 0.8f)
            }

            effectPlayer.start()
        } catch (e: Exception) {
            backgroundMusicPlayer?.setVolume(0.8f, 0.8f)
        }
    }

    fun stopMusic() {
        if (backgroundMusicPlayer != null && backgroundMusicPlayer!!.isPlaying) {
            backgroundMusicPlayer?.pause()
        }
    }

    fun resumeMusic() {
        backgroundMusicPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun release() {
        backgroundMusicPlayer?.stop()
        backgroundMusicPlayer?.release()
        backgroundMusicPlayer = null
    }
}