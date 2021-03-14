package com.example.githubusers.util.extensions

import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.remote.model.UserResponse

fun UserResponse.toLocalUser() = LocalUser(
        this.id,
        this.name,
        this.image,
        ""
)