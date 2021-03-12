package com.example.githubusers.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubusers.data.util.constants.followersCountColumn
import com.example.githubusers.data.util.constants.followingCountColumn
import com.example.githubusers.data.util.constants.idColumn
import com.example.githubusers.data.util.constants.userTableName

@Entity(tableName = userTableName)
data class LocalUser(
    @PrimaryKey
    @ColumnInfo(name = idColumn)
    @NonNull
    var id: Int? = null,
    var username: String? = null,
    var image: String? = null,
    var name: String? = null,
    @ColumnInfo(name = followersCountColumn) var followersCount: Int? = null,
    @ColumnInfo(name = followingCountColumn) var followingCount: Int? = null,
    var company: String? = null,
    var blog: String? = null,
    var notes: String? = null,
)