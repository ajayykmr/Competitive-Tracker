package com.example.competitivetracker.models.returnObjects

data class Submission(
    val author: Party = Party(),
    val contestId: Int = 0,
    val creationTimeSeconds: Int = 0,
    val id: Int = 0,
    val memoryConsumedBytes: Int = 0,
    val passedTestCount: Int = 0,
    val problem: Problem = Problem(),
    val programmingLanguage: String = "",
    val relativeTimeSeconds: Int = 0,
    val testset: String = "",
    val timeConsumedMillis: Int = 0,
    val verdict: String = ""
)