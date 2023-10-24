package com.example.competitivetracker.adapters.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.competitivetracker.R
import com.example.competitivetracker.models.returnObjects.Submission
import java.util.Locale
import kotlin.math.round

class UserSubmissionsRecyclerViewAdapter(private val submissions: List<Submission>, private val context: Context): RecyclerView.Adapter<UserSubmissionsRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProblemName: TextView = itemView.findViewById(R.id.tv_problem_name)
        val tvSubmissionID: TextView = itemView.findViewById(R.id.tv_submission_id)
        val tvSubmissionTime: TextView = itemView.findViewById(R.id.tv_submission_time)
        val tvVerdict: TextView = itemView.findViewById(R.id.tv_verdict)
        val tvRuntime: TextView = itemView.findViewById(R.id.tv_runtime)
        val tvMemory: TextView = itemView.findViewById(R.id.tv_memory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_user_submissions_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return submissions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvProblemName.text = submissions[position].problem.name
        holder.tvSubmissionID.text = submissions[position].id.toString()

        val time = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(submissions[position].creationTimeSeconds.toLong()))

//        holder.tvSubmissionTime.text = "${submissions[position].creationTimeSeconds}s"
            holder.tvSubmissionTime.text = time
        holder.tvVerdict.text = submissions[position].verdict.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        if (holder.tvVerdict.text == "Ok"){
            holder.tvVerdict.text = "Passed"
            holder.tvVerdict.setTextColor(context.getColor(R.color.green))
        } else{
            holder.tvVerdict.setTextColor(context.getColor(R.color.red))
        }
        holder.tvRuntime.text = "${submissions[position].timeConsumedMillis}ms"
        holder.tvMemory.text = "${round(submissions[position].memoryConsumedBytes / (1e6))}MB"
    }
}