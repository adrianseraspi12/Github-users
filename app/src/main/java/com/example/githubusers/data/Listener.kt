package com.example.githubusers.data

interface Listener<T: Any?> {

    fun onSuccess(data: T? = null)
    fun onFailed(errorMessage: String)

}