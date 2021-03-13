package com.example.githubusers.data.remote.model

import com.example.githubusers.util.constants.avatarUrl
import com.example.githubusers.util.constants.followers
import com.example.githubusers.util.constants.following
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName(avatarUrl) var image: String? = null,
    var name: String? = null,
    var bio: String? = null,
    @SerializedName(followers) var followersCount: Int? = null,
    @SerializedName(following) var followingCount: Int? = null,
    var company: String? = null,
    var blog: String? = null,
)