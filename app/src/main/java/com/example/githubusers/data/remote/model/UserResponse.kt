package com.example.githubusers.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
        @SerializedName("login") var name: String? = null,
        @SerializedName("avatar_url") var image: String? = null
)