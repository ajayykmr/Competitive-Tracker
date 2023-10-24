package com.example.competitivetracker.retrofit

import com.example.competitivetracker.models.ContestList
import com.example.competitivetracker.models.Leaderboard
import com.example.competitivetracker.models.UserFriends
import com.example.competitivetracker.models.UserInfo
import com.example.competitivetracker.models.UserRatingHistory
import com.example.competitivetracker.models.UserSubmissions
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    //https://codeforces.com/api/user.info?handles=DmitriyH;Fefer_Ivan;
    @GET("user.info")
    suspend fun getUserInfo(
        @Query("handles") handles: String
    ): Response<UserInfo>


    //https://codeforces.com/api/user.rating?handle=qubitt
    @GET("user.rating")
    //returns list of attended contests with 0 index as the first attended contest
    suspend fun getUserRatingHistory(
        @Query("handle") handles: String
    ): Response<UserRatingHistory>


    //https://codeforces.com/api/user.status?handle=qubitt&from=1&count=10
    @GET("user.status")
    suspend fun getUserSubmissions(
        @Query("handle") handle: String,
        @Query("from") from: Int = 1,
        @Query("count") count: Int = 30,
    ):Response<UserSubmissions>

    //https://codeforces.com/api/user.ratedList?activeOnly=true&includeRetired=false
    @GET("user.ratedList")
    suspend fun getAllLeaderboard(
        @Query("activeOnly") activeOnly: Boolean = true,
        @Query("includeRetired") includeRetired: Boolean = false,
    ): Response<Leaderboard>

    //https://codeforces.com/api/user.friends?apiKey=c155bc899e8711c9db4c0df97b2c7ce684ba1658&time=1697810073&apiSig=123456
    //03a0e8c3d8005cfa76d44f84bbe1ee23260fc65ca23d356cc4d5c5ab6acd6e67b5fc9f863ae538cb659b16bc26d28deef7d75117c346238755352fa28eec1ddc
    @GET("user.friends")
    suspend fun getUserFriends(
        @Query("apiKey") apiKey: String,
        @Query("time") time: String,
        @Query("apiSig") apiSig: String
     ): Response<UserFriends>

    @GET("contest.list")
    suspend fun getContestsList(
        @Query("gym") gym: Boolean = false
    ): Response<ContestList>

}