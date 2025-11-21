package com.example.profilecardapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [UserEntity::class, FollowerEntity::class], version = 2, exportSchema = false)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun followerDao(): FollowerDao

}