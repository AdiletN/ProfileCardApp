package com.example.profilecardapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    // Получение пользователя с id = 1
    @Query("SELECT * FROM user_table WHERE id = 1")
    suspend fun getUser(): UserEntity?

    // Вставка или замена пользователя
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Обновление данных пользователя
    @Update
    suspend fun updateUser(user: UserEntity)
}
