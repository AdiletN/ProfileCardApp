package com.example.profilecardapp.di

import android.content.Context
import androidx.room.Room
import com.example.profilecardapp.data.FollowerDao
import com.example.profilecardapp.data.ProfileDatabase
import com.example.profilecardapp.data.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ProfileDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideUserDao(database: ProfileDatabase): UserDao {
        return database.userDao()
    }
    @Provides
    fun provideFollowerDao(database: ProfileDatabase): FollowerDao {
        return database.followerDao()
    }
}