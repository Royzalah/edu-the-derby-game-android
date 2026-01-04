package com.example.myproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.adapters.ScoreAdapter
import com.example.myproject.interfaces.CallbackHighScore
import com.example.myproject.utilities.SharedPreferencesManager

class HighScoreFragment : Fragment() {

    private lateinit var main_LST_scores: RecyclerView
    var callback: CallbackHighScore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(v)
        initViews()
        return v
    }

    private fun findViews(v: View) {
        main_LST_scores = v.findViewById(R.id.main_LST_scores)
    }

    private fun initViews() {
        // Retrieve persistent scores and setup RecyclerView
        val scores = SharedPreferencesManager.getInstance().getAllScores()
        val adapter = ScoreAdapter(scores)
        adapter.callback = callback

        main_LST_scores.layoutManager = LinearLayoutManager(context)
        main_LST_scores.adapter = adapter
    }
}