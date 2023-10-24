package com.example.competitivetracker.models

import com.example.competitivetracker.models.returnObjects.User

data class Leaderboard(
    val result: List<User> = listOf(),
    val status: String = ""
)