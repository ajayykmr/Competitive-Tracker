package com.example.competitivetracker.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.competitivetracker.R
import com.example.competitivetracker.adapters.recyclerview.UserRatingHistoryRecyclerViewAdapter
import com.example.competitivetracker.models.returnObjects.RatingChanges
import com.example.competitivetracker.retrofit.RetrofitInstance
import com.example.competitivetracker.util.Constants.Companion.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserRatingChangesFragment : Fragment(R.layout.fragment_user_rating_changes) {

    private lateinit var rvUserRatingChanges: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUserRatingChanges = view.findViewById(R.id.rv_user_rating_changes)
        progressBar = view.findViewById(R.id.pb_progress_bar)
        val handle = USER_ID

        lifecycleScope.launch (Dispatchers.IO) {
            val ratingHistory = async { getUserRatingChanges(handle) }.await()

            if (!ratingHistory.isNullOrEmpty()){
                lifecycleScope.launch(Dispatchers.Main) {
                    setupRecyclerView(ratingHistory.reversed())
                }
            }
        }

    }

    private suspend fun getUserRatingChanges(handle: String): List<RatingChanges>?{
        var ratingHistory: List<RatingChanges>? = null

        val response = try {
            RetrofitInstance.api.getUserRatingHistory(handle)
        } catch (e: IOException) {
            Log.e("ERROR", "IOException, you might not have internet connection")

            lifecycleScope.launch(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Error! You might not have internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
            null
        } catch (e: HttpException) {
            lifecycleScope.launch(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error! Unexpected response", Toast.LENGTH_SHORT).show()
            }
            Log.e("ERROR", "HttpException, unexpected response")
            null
        } catch (e: Exception) {
            lifecycleScope.launch(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error! Retrofit Error", Toast.LENGTH_SHORT).show()
            }
            Log.e("ERROR", e.toString())
            null
        }

        response?.let {

            if (it.isSuccessful && it.body() != null) {
                ratingHistory=it.body()!!.result
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error! NULL User returned", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return ratingHistory
    }

    private fun setupRecyclerView(ratingHistory: List<RatingChanges>){
        rvUserRatingChanges.adapter = context?.let { it1 -> UserRatingHistoryRecyclerViewAdapter(ratingHistory, it1) }
        rvUserRatingChanges.layoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
    }
}