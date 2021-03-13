package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.Listener
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.service.GithubClient
import com.example.githubusers.data.util.constants.requestUserListErrorMessage
import com.example.githubusers.data.util.constants.requestUserProfileErrorMessage
import com.example.githubusers.data.util.constants.somethingWentWrongErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubRepository(
    private var client: GithubClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IGithubRepository {

    override suspend fun requestUserList(
        page: Int,
        listener: Listener<List<UserResponse>>
    ) = withContext(ioDispatcher) {
        client.getUserList(page).enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                //  Returns success when status code is between 200 and 299
                if (response.code() in 200..299) {
                    val listOfUserResponse = response.body()
                    listener.onSuccess(listOfUserResponse)
                }
                listener.onFailed(requestUserListErrorMessage)
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                listener.onFailed(t.message ?: somethingWentWrongErrorMessage)
            }
        })
    }

    override suspend fun requestUserProfile(
        username: String,
        listener: Listener<ProfileResponse>
    ) = withContext(ioDispatcher) {
        client.getProfile(username).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                //  Check status code
                if (response.code() == 200) {
                    val userProfileResponse = response.body()
                    listener.onSuccess(userProfileResponse)
                }
                listener.onFailed(requestUserProfileErrorMessage)
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                listener.onFailed(t.message ?: somethingWentWrongErrorMessage)
            }
        })
    }
}