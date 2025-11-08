package com.example.profilecardapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follower_table")
data class FollowerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val avatarRes: Int,
    val isFollowing: Boolean
)
