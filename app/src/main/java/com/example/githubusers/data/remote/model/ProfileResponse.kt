package com.example.githubusers.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("avatar_url") var image: String? = null,
    var name: String? = null,
    @SerializedName("followers") var followersCount: Int? = null,
    @SerializedName("following") var followingCount: Int? = null,
    var company: String? = null,
    var blog: String? = null,
)