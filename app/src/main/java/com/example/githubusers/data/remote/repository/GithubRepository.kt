package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.remote.FailedRequestException
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.service.GithubClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubRepository(private var client: GithubClient) : IGithubRepository {

    override fun requestUserList(page: Int, callback: (List<UserResponse>) -> Unit) {
        client.getUserList(page).enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
            ) {
                //  Check status code
                if (response.code() == 200) {
                    val listOfUserResponse = response.body()
                    //  Pass the response if not null otherwise
                    //  throw an exception
                    if (listOfUserResponse != null) {
                        callback(listOfUserResponse)
                    } else {
                        throw FailedRequestException()
                    }
                } else {
                    throw FailedRequestException()
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                throw FailedRequestException()
            }
        })
    }

    override fun requestUserProfile(username: String, callback: (ProfileResponse) -> Unit) {
        client.getProfile(username).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
            ) {
                //  Check status code
                if (response.code() == 200) {
                    val userProfileResponse = response.body()
                    //  Pass the response if not null otherwise
                    //  throw an exception
                    if (userProfileResponse != null) {
                        callback(userProfileResponse)
                        return
                    }
                }
                throw FailedRequestException()
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                throw FailedRequestException()
            }
        })
    }
}