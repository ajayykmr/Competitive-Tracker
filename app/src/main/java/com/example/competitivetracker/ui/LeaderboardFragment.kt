package com.example.competitivetracker.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.competitivetracker.R
import com.example.competitivetracker.adapters.recyclerview.UserListRecyclerViewAdapter
import com.example.competitivetracker.models.returnObjects.User
import com.example.competitivetracker.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    private lateinit var recyclerview: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.pb_progress_bar)
        recyclerview = view.findViewById(R.id.rv_leaderboard)

        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch (Dispatchers.IO){
            val response = try{
                RetrofitInstance.api.getAllLeaderboard()
            } catch (e: IOException) {
                Log.e("ERROR", "IOException, you might not have internet connection")
                lifecycleScope.launch (Dispatchers.Main){
                    progressBar.visibility = View.GONE
                }
                return@launch
            } catch (e: HttpException) {
                Log.e("ERROR", "HttpException, unexpected response")

                lifecycleScope.launch (Dispatchers.Main){
                    progressBar.visibility = View.GONE
//                    Toast.makeText(requireContext(), "Error! Unexpected API response", Toast.LENGTH_LONG).show()
                }

                return@launch
            } catch (e: Exception) {
                Log.e("ERROR", e.toString())
                lifecycleScope.launch (Dispatchers.Main){
                    progressBar.visibility = View.GONE
//                    Toast.makeText(requireContext(), "Error! Retrofit Error", Toast.LENGTH_LONG).show()
                }

                return@launch
            }

            response?.let {
                if (it.isSuccessful && it.body()!=null)
                {
                    lifecycleScope.launch(Dispatchers.Main){
                        progressBar.visibility = View.GONE
//                        Toast.makeText(requireContext(), "DONE", Toast.LENGTH_SHORT).show()
                        setupRecyclerView(response.body()!!.result)
                    }
                }
                else
                {
                    lifecycleScope.launch (Dispatchers.Main){
                        progressBar.visibility = View.GONE
//                        Toast.makeText(requireContext(), "NULL BODY RETURNED", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun setupRecyclerView(userList: List<User>){
        recyclerview.adapter = context?.let { it1 -> UserListRecyclerViewAdapter(userList, it1) }
        recyclerview.layoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
    }

}