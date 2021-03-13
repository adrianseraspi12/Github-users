package com.example.githubusers.data.local.repository

import com.example.githubusers.data.Listener

class MockUserRepositoryListener<T : Any?> : Listener<T> {

    var data: T? = null
    var isSuccessCalled: Boolean = false
    var errorMessage: String = ""

    override fun onSuccess(data: T?) {
        this.data = data
        this.isSuccessCalled = true
    }

    override fun onFailed(errorMessage: String) {
        this.errorMessage = errorMessage
    }
}