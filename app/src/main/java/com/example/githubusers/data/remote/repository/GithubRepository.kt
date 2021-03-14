package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.service.GithubClient
import com.example.githubusers.util.constants.requestUserListErrorMessage
import com.example.githubusers.util.constants.requestUserProfileErrorMessage
import com.example.githubusers.util.constants.somethingWentWrongErrorMessage
import kotlinx.coroutines.*
import retrofit2.Response

class GithubRepository(
        private var client: GithubClient,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IGithubRepository {

    override suspend fun requestUserList(page: Int): Result<List<UserResponse>> = withContext(ioDispatcher) {
        try {
            val response = getRequestUserListResponse(page)
            if (response.code() == 200) {
                return@withContext Result.onSuccess(response.body())
            }
            return@withContext Result.onFailed(requestUserListErrorMessage)
        } catch (t: Throwable) {
            return@withContext Result.onFailed(t.message ?: somethingWentWrongErrorMessage)
        }
    }

    override suspend fun requestUserProfile(username: String): Result<ProfileResponse> = withContext(ioDispatcher) {
        try {
            val response = getRequestUserProfile(username)
            val code = response.code()
            if (code == 200) {
                return@withContext Result.onSuccess(response.body())
            }
            return@withContext Result.onFailed(requestUserProfileErrorMessage)
        } catch (t: Throwable) {
            return@withContext Result.onFailed(t.message ?: somethingWentWrongErrorMessage)
        }
    }

    //  Created a function without suspend to
    //  Remove the warning "inappropriate blocking method call".
    //  Jetbrains inspection issue "https://youtrack.jetbrains.com/issue/KTIJ-838"
    private fun getRequestUserListResponse(page: Int): Response<List<UserResponse>> {
        return client.getUserList(page).execute()
    }

    private fun getRequestUserProfile(username: String): Response<ProfileResponse> {
        return client.getProfile(username).execute()
    }
}