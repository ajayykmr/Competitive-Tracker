package com.example.competitivetracker.models

import com.example.competitivetracker.models.returnObjects.Submission

data class UserSubmissions(
    val result: List<Submission> = listOf(),
    val status: String = ""
)