package com.example.competitivetracker.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.competitivetracker.R
import com.example.competitivetracker.models.returnObjects.User
import com.example.competitivetracker.retrofit.RetrofitInstance
import com.example.competitivetracker.util.Constants.Companion.USER_ID
import com.example.competitivetracker.util.Utils.Companion.setTextColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var etUserID: EditText
    private lateinit var tvUserName: TextView
    private lateinit var tvMaxRatingName: TextView
    private lateinit var tvMaxRatingNumber: TextView
    private lateinit var tvCurrentRatingName: TextView
    private lateinit var tvCurrentRatingNumber: TextView
    private lateinit var ivProfilePicture: ImageView
    private lateinit var progressBar: ProgressBar



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)

        progressBar.visibility = View.GONE

        if (USER_ID.isNotEmpty())
            updateHome(USER_ID)

        etUserID.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (etUserID.text.isNotEmpty()) {
                    updateHome(etUserID.text.toString())
                }
                true
            }
            false
        }
    }

    private fun updateHome(username: String) {
        progressBar.visibility = View.VISIBLE
        var currentUser: User?

        lifecycleScope.launch(Dispatchers.IO) {
            val job = async { getUser(username) }
            currentUser = job.await()


            if (currentUser == null) {
                Log.e("USER NULL", "USERN ULL")
            } else {
                lifecycleScope.launch(Dispatchers.Main)
                {
                    updateViews(currentUser!!)
                }
            }
        }
    }


    private suspend fun getUser(handle: String): User? {
        var currentUser: User? = null

        val response = try {
            RetrofitInstance.api.getUserInfo(handle)
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
                currentUser = it.body()!!.result[0]
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error! NULL User returned", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return currentUser
    }

    private fun setupViews(view: View) {
        tvCurrentRatingName = view.findViewById(R.id.tv_current_rating_name)
        tvCurrentRatingNumber = view.findViewById(R.id.tv_current_rating_number)
        tvMaxRatingName = view.findViewById(R.id.tv_max_rating_name)
        tvMaxRatingNumber = view.findViewById(R.id.tv_max_rating_number)
        etUserID = view.findViewById(R.id.tv_user_id)
        tvUserName = view.findViewById(R.id.tv_user_full_name)
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture)
        progressBar = view.findViewById(R.id.pb_progress_bar)
    }

    private fun updateViews(currentUser: User) {

        etUserID.setText(currentUser.handle)
        Glide.with(this).load(currentUser.avatar).circleCrop().into(ivProfilePicture)
        tvUserName.text = "${currentUser.firstName} ${currentUser.lastName}"
        tvMaxRatingNumber.text = currentUser.maxRating.toString()
        tvMaxRatingName.text = currentUser.maxRank.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        tvCurrentRatingName.text = currentUser.rank.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        tvCurrentRatingNumber.text = currentUser.rating.toString()


        setTextColor(requireContext(), etUserID, currentUser.rating)
        setTextColor(requireContext(), tvCurrentRatingName, currentUser.rating)
        setTextColor(requireContext(), tvCurrentRatingNumber, currentUser.rating)

        setTextColor(requireContext(), tvMaxRatingName, currentUser.maxRating)
        setTextColor(requireContext(), tvMaxRatingNumber, currentUser.maxRating)

        progressBar.visibility = View.GONE
    }


}