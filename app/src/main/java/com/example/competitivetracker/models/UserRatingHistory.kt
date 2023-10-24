package com.example.competitivetracker.models

import com.example.competitivetracker.models.returnObjects.RatingChanges

data class UserRatingHistory(
    val result: List<RatingChanges> = listOf(),
    val status: String = ""
)