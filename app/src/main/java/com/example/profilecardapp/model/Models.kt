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
data class Post(
    val id: Int,
    val userName: String,
    val userAvatarRes: Int,
    val imageRes: Int,
    val caption: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false
)
