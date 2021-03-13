package com.example.githubusers.data.remote.service

import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.util.constants.sinceQuery
import com.example.githubusers.util.constants.usernamePath
import com.example.githubusers.util.constants.users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubClient {

    @GET(users)
    fun getUserList(
        @Query(sinceQuery) page: Int,
    ): Call<List<UserResponse>>

    @GET("$users{$usernamePath}")
    fun getProfile(
        @Path(usernamePath) username: String,
    ): Call<ProfileResponse>

}