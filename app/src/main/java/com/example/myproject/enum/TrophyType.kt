package com.example.myproject.enum

import com.example.myproject.R
import com.example.myproject.utilities.Constants

// Enum defining all game collectibles with their specific properties (Score, SFX, Effects)
enum class TrophyType(
    val assetImage: Int,
    val scoreValue: Int = 0,
    val soundRes: Int,
    val isHeal: Boolean = false,
    val isShield: Boolean = false,
    val isSpeedBoost: Boolean = false,
    val effectDuration: Long = 0L
) {
    TROPHY_LIFE(R.drawable.trophy_heart, soundRes = R.raw.sound_heal, isHeal = true),

    TROPHY_BONUS(R.drawable.trophy_bonus, scoreValue = Constants.SCORE_BONUS, soundRes = R.raw.sound_bonus_bang),

    TROPHY_FAST(
        R.drawable.trophy_fast,
        soundRes = R.raw.sound_coin,
        isSpeedBoost = true,
        effectDuration = Constants.DURATION_SPEED
    ),

    TROPHY_SHIELD(
        R.drawable.trophy_shield,
        soundRes = R.raw.sound_shield_pasok,
        isShield = true,
        effectDuration = Constants.DURATION_SHIELD
    ),

    TROPHY(R.drawable.trophy, scoreValue = Constants.SCORE_REGULAR, soundRes = R.raw.sound_coin);
}