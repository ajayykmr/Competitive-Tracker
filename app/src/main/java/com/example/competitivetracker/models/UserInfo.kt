package com.example.competitivetracker.models

import com.example.competitivetracker.models.returnObjects.User

data class UserInfo(
    val result: List<User> = listOf(),
    val status: String = ""
)