package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse

interface IGithubRepository {

    suspend fun requestUserList(page: Int): Result<List<UserResponse>>
    suspend fun requestUserProfile(username: String): Result<ProfileResponse>

}