package com.example.githubusers

import android.content.Context
import com.example.githubusers.data.local.UsersDatabase
import com.example.githubusers.data.local.repository.UserRepository
import com.example.githubusers.data.remote.interceptor.GithubClientInterceptor
import com.example.githubusers.data.remote.repository.GithubRepository
import com.example.githubusers.data.remote.service.GithubClient
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {

    private const val BASE_URL = "https://api.github.com/"

    fun provideUserRepository(context: Context): UserRepository {
        val database = getDatabase(context)
        return UserRepository(database.usersDao(), database.profileDao(), Dispatchers.IO)
    }

    fun provideGithubRepository(): GithubRepository {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(GithubClientInterceptor())
            .dispatcher(dispatcher)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val githubClient = retrofit.create(GithubClient::class.java)
        return GithubRepository(githubClient, Dispatchers.IO)
    }

    private fun getDatabase(context: Context): UsersDatabase {
        return UsersDatabase.getInstance(context)
    }

}