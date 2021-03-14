package com.example.githubusers.data.main

import com.example.githubusers.data.remote.Listener

class MockMainRepositoryListener<T>: Listener<T> {

    var data: T? = null
    var isSuccess = false
    var errorMessage: String? = null


    override fun onSuccess(data: T?) {
        this.data = data
        this.isSuccess = true
    }

    override fun onFailed(errorMessage: String) {
        this.isSuccess = false
        this.errorMessage = errorMessage
    }


}