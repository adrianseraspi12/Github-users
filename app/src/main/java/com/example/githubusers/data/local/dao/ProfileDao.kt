package com.example.githubusers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile WHERE _id=:id")
    fun findProfileByUserId(id: Int): LocalUser

    @Update
    fun updateProfile(user: LocalProfile)

    @Insert
    fun insertProfile(profile: LocalProfile)

    @Query("DELETE FROM profile")
    fun deleteAll()

}