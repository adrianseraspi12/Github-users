package com.example.githubusers.data.remote.service

import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubClient {

    @GET("/user")
    fun getUserList(
        @Query("since") page: Int,
    ): Call<List<UserResponse>>

    @GET("/user/{username}")
    fun getProfile(
        @Path("username") username: String,
    ): Call<ProfileResponse>

}