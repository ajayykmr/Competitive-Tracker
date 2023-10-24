package com.example.competitivetracker.models.returnObjects

data class Contest(
    val durationSeconds: Int = 0,
    val frozen: Boolean = false,
    val id: Int = 0,
    val name: String = "",
    val phase: String = "",
    val relativeTimeSeconds: Int = 0,
    val startTimeSeconds: Int = 0,
    val type: String = ""
)