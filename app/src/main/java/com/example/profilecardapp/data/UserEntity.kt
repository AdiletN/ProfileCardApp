package com.example.profilecardapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val id: Int = 1, // у нас один пользователь, id = 1
    val name: String,
    val followerCount: Int,
    val isFollowing: Boolean
)
