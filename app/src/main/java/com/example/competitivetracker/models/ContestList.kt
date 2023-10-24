package com.example.competitivetracker.models

import com.example.competitivetracker.models.returnObjects.Contest

data class ContestList(
    val result: List<Contest> = listOf(),
    val status: String = ""
)