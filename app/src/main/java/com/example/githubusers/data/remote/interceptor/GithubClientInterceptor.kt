package com.example.githubusers.data.remote.interceptor

import com.example.githubusers.util.constants.accessToken
import com.example.githubusers.util.constants.authorizationHeader
import okhttp3.Interceptor
import okhttp3.Response

class GithubClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder().addHeader(authorizationHeader, accessToken).build()
        return chain.proceed(request)
    }
}