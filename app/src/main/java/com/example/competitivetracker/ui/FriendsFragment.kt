package com.example.competitivetracker.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.competitivetracker.R
import com.example.competitivetracker.adapters.recyclerview.UserListRecyclerViewAdapter
import com.example.competitivetracker.models.returnObjects.User
import com.example.competitivetracker.retrofit.RetrofitInstance
import com.example.competitivetracker.util.Constants.Companion.API_KEY
import com.example.competitivetracker.util.Constants.Companion.API_SECRET
import com.example.competitivetracker.util.Constants.Companion.USER_ID
import com.example.competitivetracker.util.Utils.Companion.convertTosha512
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class FriendsFragment: Fragment(R.layout.fragment_friends) {

    private lateinit var title: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerview: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.tv_friends_title)
        progressBar = view.findViewById(R.id.pb_progress_bar)
        recyclerview = view.findViewById(R.id.rv_friends)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch (Dispatchers.IO){
            val job = async { getFriendsStringList() }
            val friendsStringList = job.await()

            var handles = USER_ID
            if (friendsStringList!=null) {
                for (item in friendsStringList) {
                    handles = "$handles;$item"
                }
                val job2 = async { getUserList(handles) }
                val friendsUserList = job2.await()
                Log.e("FRIENDS", handles)
                if (friendsUserList != null) {
                    lifecycleScope.launch (Dispatchers.Main){ setupRecyclerView(friendsUserList.sortedByDescending { it.rating }) }
                }
            }
        }
    }

    private suspend fun getUserList(handles: String): List<User>? {
        var currentUser: List<User>? = null

        val response = try {
            RetrofitInstance.api.getUserInfo(handles)
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
                currentUser = it.body()!!.result
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error! NULL User returned", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return currentUser
    }

    private suspend fun getFriendsStringList(): List<String>?{

        var answer: List<String>? = null

        val unixTime = System.currentTimeMillis() / 1000
        val random = "123456"
        val convertToHex = "${random}/user.friends?apiKey=${API_KEY}&time=${unixTime}#${API_SECRET}"
        val convertedHEX = convertTosha512(convertToHex).lowercase(Locale.getDefault())
        val apiSig = "${random}${convertedHEX}"

        val response = try {
            RetrofitInstance.api.getUserFriends(API_KEY, unixTime.toString(), apiSig)
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
                Log.e("FRIENDS", "friendsStringList Received")
                answer = it.body()!!.result
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Please enter correct API Key and Secret", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
        return answer
    }

    private fun setupRecyclerView(userList: List<User>){
        recyclerview.adapter = context?.let { it1 -> UserListRecyclerViewAdapter(userList, it1) }
        recyclerview.layoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
    }

}