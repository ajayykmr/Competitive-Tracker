package com.example.competitivetracker.adapters.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.competitivetracker.R
import com.example.competitivetracker.models.returnObjects.RatingChanges
import com.example.competitivetracker.util.Utils

class UserRatingHistoryRecyclerViewAdapter(private val ratingHistory: List<RatingChanges>, private val context: Context): RecyclerView.Adapter<UserRatingHistoryRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContestNumber: TextView = itemView.findViewById(R.id.tv_contest_number)
        val tvContestName: TextView = itemView.findViewById(R.id.tv_contest_name)
        val tvRank: TextView = itemView.findViewById(R.id.tv_rank)
        val tvContestDate: TextView = itemView.findViewById(R.id.tv_contest_date)

        val tvCurrentRatingNumber: TextView = itemView.findViewById(R.id.tv_current_rating_number)
        val tvDelta: TextView = itemView.findViewById(R.id.tv_delta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_user_rating_changes_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ratingHistory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvContestNumber.text = "${ratingHistory.size - position}. "
        holder.tvContestName.text = ratingHistory[position].contestName

        holder.tvRank.text = ratingHistory[position].rank.toString()

        val time = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(ratingHistory[position].ratingUpdateTimeSeconds.toLong()))


//        holder.tvContestDate.text = ratingHistory[position].ratingUpdateTimeSeconds.toString()
        holder.tvContestDate.text = time
        holder.tvCurrentRatingNumber.text = ratingHistory[position].newRating.toString()

        val delta = ratingHistory[position].newRating - ratingHistory[position].oldRating

        if (delta<0)
            holder.tvDelta.text = delta.toString()
        else{
            holder.tvDelta.text = "+${delta}"
            holder.tvDelta.setTextColor(context.getColor(R.color.green))

        }

        Utils.setTextColor(context, holder.tvCurrentRatingNumber, ratingHistory[position].newRating)

    }
}

