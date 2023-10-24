package com.example.competitivetracker.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.competitivetracker.R
import com.example.competitivetracker.util.Constants.Companion.API_KEY
import com.example.competitivetracker.util.Constants.Companion.API_SECRET
import com.example.competitivetracker.util.Constants.Companion.USER_ID

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private lateinit var etUserID: EditText
    private lateinit var etApiKey: EditText
    private lateinit var etSecret: EditText

    private lateinit var btnSave: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sharedPref = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        etUserID = view.findViewById(R.id.et_user_id)
        etApiKey = view.findViewById(R.id.et_api_key)
        etSecret = view.findViewById(R.id.et_api_secret)
        btnSave = view.findViewById(R.id.btn_save)


        if (sharedPref!=null)
        {
            etUserID.setText(sharedPref.getString("userID", "").toString())
            etApiKey.setText(sharedPref.getString("apiKey", "").toString())
            etSecret.setText(sharedPref.getString("apiSecret", "").toString())
        }



        btnSave.setOnClickListener {

            if ((etUserID.text.isNotEmpty() && etApiKey.text.isEmpty() && etSecret.text.isEmpty())
                ||
                (etUserID.text.isNotEmpty() && etApiKey.text.isNotEmpty() && etSecret.text.isNotEmpty())
                ){
                USER_ID = etUserID.text.toString()
                API_KEY = etApiKey.text.toString()
                API_SECRET = etSecret.text.toString()

                editor?.apply {
                    putString("userID", etUserID.text.toString())
                    putString("apiKey", etApiKey.text.toString())
                    putString("apiSecret", etSecret.text.toString())
                    apply()

                    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(context, "Invalid Inputs", Toast.LENGTH_SHORT).show()
            }

        }
    }
}