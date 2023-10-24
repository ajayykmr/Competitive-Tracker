package com.example.competitivetracker.models.returnObjects

data class Party(
    val contestId: Int = 0,
    val ghost: Boolean = false,
    val members: List<Member> = listOf(),
    val participantType: String = "",
    val room: Int = 0,
    val startTimeSeconds: Int = 0
)