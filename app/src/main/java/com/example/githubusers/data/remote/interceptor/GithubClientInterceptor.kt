package com.example.githubusers.data.remote.interceptor

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.githubusers.data.remote.NoInternetConnectionException
import com.example.githubusers.util.constants.accessToken
import com.example.githubusers.util.constants.authorizationHeader
import okhttp3.Interceptor
import okhttp3.Response

class GithubClientInterceptor(private val connectivityManager: ConnectivityManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnectedToNetwork()) throw NoInternetConnectionException()
        val request =
                chain.request().newBuilder().addHeader(authorizationHeader, accessToken).build()
        return chain.proceed(request)
    }

    private fun isConnectedToNetwork(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

}