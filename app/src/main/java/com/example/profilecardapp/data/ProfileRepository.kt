package com.example.profilecardapp.data

import com.example.profilecardapp.R
import com.example.profilecardapp.model.Follower
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val userDao: UserDao,
    private val followerDao: FollowerDao
) {

    // --- User operations ---
    suspend fun getUser(): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUser()
    }

    suspend fun insertUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    // --- Followers operations ---
    suspend fun getFollowers(): List<FollowerEntity> = withContext(Dispatchers.IO) {
        followerDao.getAll()
    }

    suspend fun insertFollower(follower: FollowerEntity) = withContext(Dispatchers.IO) {
        followerDao.insert(follower)
    }

    suspend fun updateFollower(follower: FollowerEntity) = withContext(Dispatchers.IO) {
        followerDao.update(follower)
    }

    suspend fun deleteFollower(follower: FollowerEntity) = withContext(Dispatchers.IO) {
        followerDao.delete(follower)
    }

    // --- Refresh user from fake API ---
    suspend fun refreshUserFromApi() = withContext(Dispatchers.IO) {
        val users = RetrofitInstance.api.getUsers()
        if (users.isNotEmpty()) {
            val first = users.first() // берём первого пользователя для примера
            val entity = UserEntity(
                id = 1,
                name = first.name,
                followerCount = 100,
                isFollowing = false
            )
            userDao.insertUser(entity)
        }
    }
    suspend fun refreshFollowersFromApi() = withContext(Dispatchers.IO) {
        val users = RetrofitInstance.api.getUsers()
        users.forEach { user ->
            val follower = FollowerEntity(
                name = user.name,
                avatarRes = R.drawable.profile_image,
                isFollowing = false
            )
            followerDao.insert(follower) // insert с REPLACE уже стоит в FollowerDao
        }
    }
}

// --- Extension function для конвертации Follower -> FollowerEntity ---
fun Follower.toEntity(): FollowerEntity = FollowerEntity(
    name = this.name,
    avatarRes = this.avatarRes,
    isFollowing = this.isFollowing
)
