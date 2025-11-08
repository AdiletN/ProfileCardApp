package com.example.profilecardapp.data

import retrofit2.http.GET

data class UserDto(
    val id: Int,
    val name: String
)

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}
