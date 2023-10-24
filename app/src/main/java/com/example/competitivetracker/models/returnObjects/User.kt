package com.example.competitivetracker.models.returnObjects

data class User(
    val avatar: String = "",
    val city: String = "",
    val contribution: Int = 0,
    val country: String = "",
    val email: String = "",
    val firstName: String = "",
    val friendOfCount: Int = 0,
    val handle: String = "",
    val lastName: String = "",
    val lastOnlineTimeSeconds: Int = 0,
    val maxRank: String = "",
    val maxRating: Int = 0,
    val organization: String = "",
    val rank: String = "",
    val rating: Int = 0,
    val registrationTimeSeconds: Int = 0,
    val titlePhoto: String = "",
    val vkId: String = ""
)