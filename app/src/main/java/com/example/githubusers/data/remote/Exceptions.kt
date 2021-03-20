package com.example.githubusers.data.remote

import com.example.githubusers.util.constants.noInternetConnectionErrorMessage
import java.lang.Exception

class NoInternetConnectionException: Exception() {
    override val message: String get() = noInternetConnectionErrorMessage
}