package com.example.githubusers.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubusers.util.constants.idColumn
import com.example.githubusers.util.constants.userTableName

@Entity(tableName = userTableName)
data class LocalUser(
        @PrimaryKey
        @ColumnInfo(name = idColumn)
        @NonNull
        var id: Int? = null,
        var username: String? = null,
        var image: String? = null,
        var notes: String? = null,
)