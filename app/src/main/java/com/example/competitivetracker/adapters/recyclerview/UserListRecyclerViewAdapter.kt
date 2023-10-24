package com.example.competitivetracker.adapters.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.competitivetracker.R
import com.example.competitivetracker.models.returnObjects.User
import com.example.competitivetracker.util.Utils
import java.util.Locale

class UserListRecyclerViewAdapter(private val userList: List<User>, private val context: Context): RecyclerView.Adapter<UserListRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvUserID: TextView = itemView.findViewById(R.id.tv_user_id)
        val tvUserName: TextView = itemView.findViewById(R.id.tv_user_full_name)
        val tvRatingName: TextView = itemView.findViewById(R.id.tv_current_rating_name)
        val tvRatingNumber: TextView = itemView.findViewById(R.id.tv_current_rating_number)
        val ivProfilePicture: ImageView = itemView.findViewById(R.id.iv_profile_picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_user_list_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUserID.text = userList[position].handle

        holder.tvUserName.text = "${userList[position].firstName.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }} ${userList[position].lastName.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"

        holder.tvRatingNumber.text = userList[position].rating.toString()
        holder.tvRatingName.text = userList[position].rank
        Glide.with(context).load(userList[position].avatar).circleCrop().into(holder.ivProfilePicture)

        Utils.setTextColor(context, holder.tvUserID, userList[position].rating)
        Utils.setTextColor(context, holder.tvRatingName, userList[position].rating)
        Utils.setTextColor(context, holder.tvRatingNumber, userList[position].rating)
    }
}