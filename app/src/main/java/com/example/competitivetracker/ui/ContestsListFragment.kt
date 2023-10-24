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
import com.example.competitivetracker.adapters.recyclerview.ContestsListRecyclerViewAdapter
import com.example.competitivetracker.models.returnObjects.Contest
import com.example.competitivetracker.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ContestsListFragment: Fragment(R.layout.fragment_contests_list) {

    private lateinit var rvContestsList: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvContestsList = view.findViewById(R.id.rv_contests_list)
        progressBar = view.findViewById(R.id.pb_progress_bar)


        lifecycleScope.launch(Dispatchers.IO){
            val contests = async { getContestsList() }.await()

            if (!contests.isNullOrEmpty()){
                lifecycleScope.launch(Dispatchers.Main) {
                    setupRecyclerView(contests)
                }
            }
        }
    }

    private suspend fun getContestsList(): List<Contest>?{
        var contests: List<Contest>? = null

        val response = try {
            RetrofitInstance.api.getContestsList()
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
                contests=it.body()!!.result
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error! NULL User returned", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return contests
    }

    private fun setupRecyclerView(contests: List<Contest>){
        rvContestsList.adapter = context?.let { it1 -> ContestsListRecyclerViewAdapter(contests, it1) }
        rvContestsList.layoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
    }
}