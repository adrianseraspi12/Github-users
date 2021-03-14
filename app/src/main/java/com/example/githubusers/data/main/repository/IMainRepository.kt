package com.example.githubusers.data.main.repository

import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.remote.Listener

interface IMainRepository {

    suspend fun loadUserList(listener: Listener<List<UserWithProfile>>)

    suspend fun updateUserOnLocal(user: LocalUser, listener: Listener<Nothing>)

}