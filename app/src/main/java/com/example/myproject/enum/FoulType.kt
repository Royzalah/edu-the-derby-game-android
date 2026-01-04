package com.example.myproject.enum

import com.example.myproject.R

// Enum class to manage referee foul images
enum class FoulType(val drawableRes: Int) {
    FOUL1(R.drawable.ref_foul1), // Image for 1st foul (2 lives left)
    FOUL2(R.drawable.ref_foul2), // Image for 2nd foul (1 life left)
    FOUL3(R.drawable.ref_foul3); // Image for 3rd foul (Game Over)
}