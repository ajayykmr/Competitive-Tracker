package com.example.competitivetracker.models.returnObjects

data class Problem(
    val contestId: Int = 0,
    val index: String = "",
    val name: String = "",
    val points: Double = 0.0,
    val rating: Int = 0,
    val tags: List<String> = listOf(),
    val type: String = ""
)