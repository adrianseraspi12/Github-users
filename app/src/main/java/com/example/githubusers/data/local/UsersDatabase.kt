package com.example.githubusers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubusers.data.local.dao.UsersDao
import com.example.githubusers.data.local.entity.LocalUser

@Database(entities = [LocalUser::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}