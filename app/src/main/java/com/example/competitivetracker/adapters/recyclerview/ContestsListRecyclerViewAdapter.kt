package com.example.competitivetracker.adapters.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.competitivetracker.R
import com.example.competitivetracker.models.returnObjects.Contest

class ContestsListRecyclerViewAdapter(private val contests: List<Contest>, private val context: Context): RecyclerView.Adapter<ContestsListRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContestName: TextView = itemView.findViewById(R.id.tv_contest_name)
        val tvDate: TextView = itemView.findViewById(R.id.tv_contest_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_contests_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contests.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvContestName.text = contests[position].name

        val time = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(contests[position].startTimeSeconds.toLong()))

        holder.tvDate.text = time.toString()
//        holder.tvDate.text = contests[position].startTimeSeconds.toString()
    }
}

