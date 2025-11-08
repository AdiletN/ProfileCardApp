package com.example.profilecardapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.profilecardapp.model.Follower
import com.example.profilecardapp.model.Story
import com.example.profilecardapp.R

class ProfileViewModel : ViewModel() {

    var name by mutableStateOf("Adilet Naurzalin")
        private set

    var bio by mutableStateOf("Java Developer - Beginner")
        private set

    var followerCount by mutableStateOf(100)
        private set

    var isFollowing by mutableStateOf(false)
        private set

    var profileUpdatedCounter by mutableStateOf(0)
        private set
    private val _followers = mutableStateListOf(
        Follower("Aruzhan", R.drawable.profile_image2, false),
        Follower("Dias", R.drawable.profile_image3, false),
        Follower("Ayazhan", R.drawable.profile_image2, true)
    )
    val followers: List<Follower> get() = _followers

    private var recentlyRemoved: Follower? = null

    private val _stories = listOf(
        Story("Aruzhan", R.drawable.profile_image2),
        Story("Dias", R.drawable.profile_image3),
        Story("Ayazhan", R.drawable.profile_image2)
    )
    val stories: List<Story> get() = _stories

    fun updateName(newName: String) {
        name = newName
        profileUpdatedCounter++
    }

    fun updateBio(newBio: String) {
        bio = newBio
        profileUpdatedCounter++
    }

    fun updateProfile(newName: String, newBio: String) {
        name = newName
        bio = newBio
        profileUpdatedCounter++
    }

    fun toggleFollowMain() {
        if (!isFollowing) {
            isFollowing = true
            followerCount++
        } else {
            isFollowing = false
            followerCount--
        }
    }

    fun addFollower(follower: Follower) {
        _followers.add(follower)
    }

    fun removeFollower(follower: Follower) {
        recentlyRemoved = follower
        _followers.removeAll { it.name == follower.name }
    }

    fun undoRemove() {
        recentlyRemoved?.let { _followers.add(it) }
        recentlyRemoved = null
    }

    fun toggleFollowerFollowState(name: String) {
        val index = _followers.indexOfFirst { it.name == name }
        if (index >= 0) {
            val f = _followers[index]
            _followers[index] = f.copy(isFollowing = !f.isFollowing)
        }
    }
}
