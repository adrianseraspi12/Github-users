package com.example.githubusers.util.extensions

import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.remote.model.ProfileResponse

fun ProfileResponse.toLocalProfile(userId: Int?) = LocalProfile(
        userId,
        userId,
        this.name,
        this.bio,
        this.followersCount,
        this.followingCount,
        this.company,
        this.blog
)