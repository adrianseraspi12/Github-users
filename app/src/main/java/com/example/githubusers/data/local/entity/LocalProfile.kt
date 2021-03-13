package com.example.githubusers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubusers.util.constants.*

@Entity(tableName = profileTableName)
data class LocalProfile(
    @ColumnInfo(name = idColumn)
    @PrimaryKey var profileId: Int? = null,
    @ColumnInfo(name = userIdColumn)
    var userId: Int? = null,
    var name: String? = null,
    var bio: String? = null,
    @ColumnInfo(name = followersCountColumn) var followersCount: Int? = null,
    @ColumnInfo(name = followingCountColumn) var followingCount: Int? = null,
    var company: String? = null,
    var blog: String? = null,
)