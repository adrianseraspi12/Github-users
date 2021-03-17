package com.example.githubusers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.githubusers.data.local.entity.LocalProfile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile WHERE _id=:id")
    fun findProfileByUserId(id: Int): LocalProfile

    @Update
    fun updateProfile(user: LocalProfile)

    @Insert(onConflict = REPLACE)
    fun insertProfile(profile: LocalProfile)

    @Query("DELETE FROM profile")
    fun deleteAll()

}