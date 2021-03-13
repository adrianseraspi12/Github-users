package com.example.githubusers.data.local.repository

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile

interface IUserRepository {

    suspend fun getAllUsers(): Result<List<LocalUser>>

    suspend fun getAllUserWithProfile(): Result<List<UserWithProfile>>

    suspend fun getUser(id: Int): Result<LocalUser>

    suspend fun insertProfile(profile: LocalProfile): Result<Nothing>

    suspend fun updateProfile(profile: LocalProfile): Result<Nothing>

    suspend fun updateUser(user: LocalUser): Result<Nothing>

    suspend fun saveAll(user: List<LocalUser>): Result<Nothing>

    suspend fun deleteAllUser(): Result<Nothing>
}