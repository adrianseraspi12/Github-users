package com.example.githubusers.data.remote.model

import com.example.githubusers.util.constants.avatarUrl
import com.example.githubusers.util.constants.login
import com.google.gson.annotations.SerializedName

data class UserResponse(
        var id: Int? = null,
        @SerializedName(login) var name: String? = null,
        @SerializedName(avatarUrl) var image: String? = null
)