package com.example.hobbyyk_new.data.remote

import com.example.hobbyyk_new.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 403 || response.code == 401) {
            SessionManager.logout()
        }

        return response
    }
}