package com.example.myproject.enum

import com.example.myproject.R

// Enum class for different types of defenders

enum class DefenderType(val drawableResources: Int) {
    OFEK_DEFENDER(R.drawable.ofek_defender),
    MATAN_DEFENDER(R.drawable.matan_defender),
    DODI_DEFENDER(R.drawable.dodi_defender),
    ALON_DEFENDER(R.drawable.alon_defender),
    LIOR_DEFENDER(R.drawable.lior_defender),
    BAR_DEFENDER(R.drawable.bar_defender);

    companion object {
        // Returns a random DefenderType from the available entries.
        fun getRandom(): DefenderType {
            return entries.random()
        }
    }
}