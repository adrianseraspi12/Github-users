package com.example.githubusers.data.local.repository

import com.example.githubusers.data.local.entity.LocalUser

interface IUserRepository {

    interface Listener<T : Any?> {

        fun onSuccess(data: T? = null)
        fun onFailed(errorMessage: String)

    }

    suspend fun getAllUsers(listener: Listener<List<LocalUser>>)

    suspend fun getUser(id: Int, listener: Listener<LocalUser>)

    suspend fun updateUser(user: LocalUser, listener: Listener<Any>)

    suspend fun saveAll(user: List<LocalUser>, listener: Listener<Any>)

    suspend fun deleteAllUser(listener: Listener<Any>)
}