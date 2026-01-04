package com.example.myproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.interfaces.CallbackHighScore
import com.example.myproject.utilities.Score


class ScoreAdapter(private val scores: ArrayList<Score>) :
    RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    var callback: CallbackHighScore? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.score_item, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]

        holder.score_LBL_name.text = "${position + 1}. ${score.name}"
        holder.score_LBL_score.text = "${score.score}"

        // Highlight the top 3 players with specific trophy icons
        when (position) {
            0 -> {
                holder.score_IMG_trophy.setImageResource(R.drawable.winner)
                holder.score_IMG_trophy.visibility = View.VISIBLE
            }
            1 -> {
                holder.score_IMG_trophy.setImageResource(R.drawable.sec_place)
                holder.score_IMG_trophy.visibility = View.VISIBLE
            }
            2 -> {
                holder.score_IMG_trophy.setImageResource(R.drawable.third_place)
                holder.score_IMG_trophy.visibility = View.VISIBLE
            }
            else -> {
                holder.score_IMG_trophy.visibility = View.INVISIBLE
            }
        }

        // Trigger callback to show location on map when clicked
        holder.itemView.setOnClickListener {
            callback?.highScoreItemClicked(score.lat, score.lon)
        }
    }

    override fun getItemCount(): Int {
        return scores.size
    }

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val score_LBL_name: TextView = itemView.findViewById(R.id.score_LBL_name)
        val score_LBL_score: TextView = itemView.findViewById(R.id.score_LBL_score)
        val score_IMG_trophy: ImageView = itemView.findViewById(R.id.score_IMG_trophy)
    }
}