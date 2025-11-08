package com.example.profilecardapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface FollowerDao {

    @Query("SELECT * FROM follower_table")
    suspend fun getAll(): List<FollowerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(follower: FollowerEntity)

    @Update
    suspend fun update(follower: FollowerEntity)

    @Delete
    suspend fun delete(follower: FollowerEntity)
}
