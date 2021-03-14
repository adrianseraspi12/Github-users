package com.example.githubusers.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubusers.data.local.dao.ProfileDao
import com.example.githubusers.data.local.dao.UsersDao
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser

@Database(entities = [LocalUser::class, LocalProfile::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun profileDao(): ProfileDao

    companion object {

        private const val DATABASE_NAME = "local_storage.db"

        fun getInstance(context: Context): UsersDatabase {
            return Room.databaseBuilder(
                    context,
                    UsersDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}