package com.example.githubusers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<LocalUser>

    @Query("SELECT * FROM users WHERE _id=:id")
    fun findUserById(id: Int): LocalUser

    @Update
    fun updateUser(user: LocalUser)

    @Insert
    fun insertAll(listOfUSer: List<LocalUser>)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("SELECT * FROM users")
    fun getAllUserWithProfile(): List<UserWithProfile>
}