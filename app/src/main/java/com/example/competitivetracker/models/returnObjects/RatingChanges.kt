package com.example.competitivetracker.models.returnObjects

data class RatingChanges(
    val contestId: Int = 0,
    val contestName: String = "",
    val handle: String = "",
    val newRating: Int = 0,
    val oldRating: Int = 0,
    val rank: Int = 0,
    val ratingUpdateTimeSeconds: Int = 0
)