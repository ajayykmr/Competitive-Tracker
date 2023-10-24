package com.example.competitivetracker.util

import android.content.Context
import android.widget.TextView
import com.example.competitivetracker.R
import java.security.MessageDigest

class Utils {
    companion object{
        fun convertTosha512(input: String) = hashString("SHA-512", input)
        fun sha256(input: String) = hashString("SHA-256", input)
        fun sha1(input: String) = hashString("SHA-1", input)

        private fun hashString(type: String, input: String): String {
            val HEX_CHARS = "0123456789ABCDEF"
            val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
            val result = StringBuilder(bytes.size * 2)
            bytes.forEach {
                val i = it.toInt()
                result.append(HEX_CHARS[i shr 4 and 0x0f])
                result.append(HEX_CHARS[i and 0x0f])
            }
            return result.toString()
        }

        fun setTextColor(context: Context, view: TextView, rating: Int) {
            view.setTextColor( context.getColor(
                when(rating){
                    in 0..1199 -> {
                        R.color.newbie
                    }
                    in 1200..1399 -> {
                        R.color.pupil
                    }
                    in 1400..1599 -> {
                        R.color.specialist
                    }
                    in 1600..1899 -> {
                        R.color.expert
                    }
                    in 1900..2099  -> {
                        R.color.candidate_master
                    }
                    in 2100..2299  -> {
                        R.color.master
                    }
                    in 2300..2400 -> {
                        R.color.international_master
                    }
                    in 2400..2599  -> {
                        R.color.grandmaster
                    }
                    in 2600..2999 -> {
                        R.color.international_grandmaster
                    }
                    else -> {
                        R.color.legendary_grandmaster
                    }
                }
            ))

        }
    }
}