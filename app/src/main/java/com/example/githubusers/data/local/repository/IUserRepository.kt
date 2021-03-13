package com.example.githubusers.data.local.repository

import com.example.githubusers.data.Listener
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile

interface IUserRepository {

    suspend fun getAllUsers(listener: Listener<List<LocalUser>>)

    suspend fun getAllUserWithProfile(listener: Listener<List<UserWithProfile>>)

    suspend fun getUser(id: Int, listener: Listener<LocalUser>)

    suspend fun insertProfile(profile: LocalProfile, listener: Listener<Any>)

    suspend fun updateProfile(profile: LocalProfile, listener: Listener<Any>)

    suspend fun updateUser(user: LocalUser, listener: Listener<Any>)

    suspend fun saveAll(user: List<LocalUser>, listener: Listener<Any>)

    suspend fun deleteAllUser(listener: Listener<Any>)
}