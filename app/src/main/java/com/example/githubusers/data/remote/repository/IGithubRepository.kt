package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse

interface IGithubRepository {

    fun requestUserList(page: Int, callback: ((List<UserResponse>) -> Unit))
    fun requestUserProfile(username: String, callback: ((ProfileResponse) -> Unit))

}