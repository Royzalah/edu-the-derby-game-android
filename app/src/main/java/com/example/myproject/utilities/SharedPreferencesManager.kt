package com.example.myproject.utilities

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance ?: throw IllegalStateException("SharedPreferencesManager not initialized")
        }
    }

    fun getAllScores(): ArrayList<Score> {
        val json = sharedPreferences.getString("SCORES", null)
        val type = object : TypeToken<ArrayList<Score>>() {}.type
        return if (json == null) ArrayList() else gson.fromJson(json, type)
    }

    // Saves score while maintaining a strict "Top 10" list
    fun saveScore(newScore: Score) {
        val scores = getAllScores()
        scores.add(newScore)

        // Sort descending by score value
        scores.sortByDescending { it.score }

        // Remove the lowest score if list exceeds 10
        if (scores.size > 10) {
            scores.removeAt(scores.size - 1)
        }

        val json = gson.toJson(scores)
        sharedPreferences.edit().putString("SCORES", json).apply()
    }
}