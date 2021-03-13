package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.remote.Listener
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse

interface IGithubRepository {

    suspend fun requestUserList(page: Int, listener: Listener<List<UserResponse>>)
    suspend fun requestUserProfile(username: String, listener: Listener<ProfileResponse>)

}