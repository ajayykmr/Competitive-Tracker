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
import com.example.competitivetracker.adapters.recyclerview.UserSubmissionsRecyclerViewAdapter
import com.example.competitivetracker.models.returnObjects.Submission
import com.example.competitivetracker.retrofit.RetrofitInstance
import com.example.competitivetracker.util.Constants.Companion.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserSubmissionsFragment: Fragment(R.layout.fragment_user_submissions) {

    private lateinit var rvUserSubmissions: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUserSubmissions = view.findViewById(R.id.rv_user_submissions)
        progressBar = view.findViewById(R.id.pb_progress_bar)

        val username = USER_ID

        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch (Dispatchers.IO){
            val job = async { getSubmissions(username) }
            val submissions = job.await()

            if (!submissions.isNullOrEmpty()){
                lifecycleScope.launch(Dispatchers.Main){
                    setupRecyclerView(submissions)
                }
            }
            else
            {
                lifecycleScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Received 0 submissions", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView(submissions: List<Submission>) {
        rvUserSubmissions.adapter =
            context?.let { UserSubmissionsRecyclerViewAdapter(submissions, it) }
        rvUserSubmissions.layoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
    }

    private suspend fun getSubmissions(handle: String): List<Submission>?{

        var submissionList: List<Submission>? = null

        val response = try {
            RetrofitInstance.api.getUserSubmissions(handle)
        } catch (e: IOException) {
            Log.e("ERROR", "IOException, you might not have internet connection")

            lifecycleScope.launch(Dispatchers.Main) {
//                progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Error! You might not have internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
            null
        } catch (e: HttpException) {
            lifecycleScope.launch(Dispatchers.Main) {
//                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error! Unexpected response", Toast.LENGTH_SHORT).show()
            }
            Log.e("ERROR", "HttpException, unexpected response")
            null
        } catch (e: Exception) {
            lifecycleScope.launch(Dispatchers.Main) {
//                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error! Retrofit Error", Toast.LENGTH_SHORT).show()
            }
            Log.e("ERROR", e.toString())
            null
        }

        response?.let {

            if (it.isSuccessful && it.body() != null) {
                submissionList = it.body()!!.result
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
//                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error! NULL User returned", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return submissionList
    }

}