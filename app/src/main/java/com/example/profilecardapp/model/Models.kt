package com.example.profilecardapp.model

data class Follower(
    val name: String,
    val avatarRes: Int,
    val isFollowing: Boolean
)

data class Story(
    val name: String,
    val avatarRes: Int
)
