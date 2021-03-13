package com.example.githubusers.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.githubusers.data.util.constants.idColumn
import com.example.githubusers.data.util.constants.userIdColumn

data class UserWithProfile(
        @Embedded
        var user: LocalUser? = null,
        @Relation(parentColumn = idColumn, entityColumn = userIdColumn)
        var profile: LocalProfile? = null
)