package com.example.profilecardapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profilecardapp.data.ProfileRepository
import com.example.profilecardapp.data.UserEntity
import com.example.profilecardapp.data.toEntity
import com.example.profilecardapp.model.Follower
import com.example.profilecardapp.model.Story
import com.example.profilecardapp.R
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // --- User profile state ---
    var name by mutableStateOf("")
        private set

    var bio by mutableStateOf("")
        private set

    var followerCount by mutableStateOf(0)
        private set

    var isFollowing by mutableStateOf(false)
        private set

    var profileUpdatedCounter by mutableStateOf(0)
        private set

    // --- Followers and stories ---
    private val _followers = mutableStateListOf<Follower>()
    val followers: List<Follower> get() = _followers

    private var recentlyRemoved: Follower? = null

    private val _stories = mutableStateListOf(
        Story("Aruzhan", R.drawable.profile_image2),
        Story("Dias", R.drawable.profile_image3),
        Story("Ayazhan", R.drawable.profile_image2)
    )
    val stories: List<Story> get() = _stories

    // --- Initialization: load user and followers from Room ---
    init {
        loadUserFromDb()
        loadFollowersFromDb()
    }

    private fun loadUserFromDb() {
        viewModelScope.launch {
            val user = repository.getUser()
            if (user != null) {
                name = user.name
                followerCount = user.followerCount
                isFollowing = user.isFollowing
            } else {
                val defaultUser = UserEntity(
                    id = 1,
                    name = "Adilet Naurzalin",
                    followerCount = 100,
                    isFollowing = false
                )
                repository.insertUser(defaultUser)
                name = defaultUser.name
                followerCount = defaultUser.followerCount
                isFollowing = defaultUser.isFollowing
            }
        }
    }

    private fun loadFollowersFromDb() {
        viewModelScope.launch {
            val savedFollowers = repository.getFollowers()
            _followers.clear()
            _followers.addAll(savedFollowers.map { Follower(it.name, it.avatarRes, it.isFollowing) })
        }
    }
    fun refreshFollowersFromApi() {
        viewModelScope.launch {
            try {
                repository.refreshFollowersFromApi()
                loadFollowersFromDb() // обновляем список после вставки
            } catch (e: Exception) {
                e.printStackTrace() // чтобы не падало, если что-то с API
            }
        }
    }

    // --- Profile update methods with Room persistence ---
    fun updateName(newName: String) {
        name = newName
        profileUpdatedCounter++
        saveUserToDb()
    }

    fun updateBio(newBio: String) {
        bio = newBio
        profileUpdatedCounter++
        saveUserToDb()
    }

    fun updateProfile(newName: String, newBio: String) {
        name = newName
        bio = newBio
        profileUpdatedCounter++
        saveUserToDb()
    }

    fun toggleFollowMain() {
        if (!isFollowing) {
            isFollowing = true
            followerCount++
        } else {
            isFollowing = false
            followerCount--
        }
        saveUserToDb()
    }

    private fun saveUserToDb() {
        viewModelScope.launch {
            repository.updateUser(
                UserEntity(
                    id = 1,
                    name = name,
                    followerCount = followerCount,
                    isFollowing = isFollowing
                )
            )
        }
    }

    // --- Followers list operations ---
    fun addFollower(follower: Follower) {
        _followers.add(follower)
        viewModelScope.launch { repository.insertFollower(follower.toEntity()) }
    }

    fun removeFollower(follower: Follower) {
        recentlyRemoved = follower
        _followers.removeAll { it.name == follower.name }
        viewModelScope.launch { repository.deleteFollower(follower.toEntity()) }
    }

    fun undoRemove() {
        recentlyRemoved?.let {
            _followers.add(it)
            viewModelScope.launch { repository.insertFollower(it.toEntity()) }
        }
        recentlyRemoved = null
    }

    fun toggleFollowerFollowState(name: String) {
        val index = _followers.indexOfFirst { it.name == name }
        if (index >= 0) {
            val f = _followers[index]
            val updated = f.copy(isFollowing = !f.isFollowing)
            _followers[index] = updated
            viewModelScope.launch { repository.updateFollower(updated.toEntity()) }
        }
    }

    // --- Refresh data from API ---
    fun refreshFromApi() {
        viewModelScope.launch {
            repository.refreshUserFromApi()
            val user = repository.getUser()
            if (user != null) {
                name = user.name
                followerCount = user.followerCount
                isFollowing = user.isFollowing
            }
        }
    }
}
