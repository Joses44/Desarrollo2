package com.example.desarrollo.network

import com.example.desarrollo.data.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { authManager.getToken().first() }

        // 🎯 SOLO añade el header si el token REALMENTE existe y no es la palabra "null"
        if (!token.isNullOrBlank() && token != "null") {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }

        // Si no hay token, envía la petición limpia (sin header Authorization)
        return chain.proceed(originalRequest)
    }
}