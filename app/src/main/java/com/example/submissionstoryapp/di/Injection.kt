package com.example.submissionstoryapp.di

import android.content.Context
import com.example.submissionstoryapp.data.pref.UserPreference
import com.example.submissionstoryapp.data.pref.dataStore
import com.example.submissionstoryapp.data.remote.StoriesRepository
import com.example.submissionstoryapp.data.remote.UserRepository
import com.example.submissionstoryapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, user)
    }

    fun provideStoriesRepository(context: Context): StoriesRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = ApiConfig.getApiService(pref)
        return StoriesRepository.getInstance(user)
    }
}