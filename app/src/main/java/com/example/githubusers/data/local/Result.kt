package com.example.githubusers.data.local

sealed class Result<out T> {

    data class onSuccess<out T>(val data: T? = null): Result<T>()
    data class onFailed(val errorMessage: String): Result<Nothing>()

}