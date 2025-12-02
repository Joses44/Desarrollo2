package com.example.desarrollo.network

import com.example.desarrollo.data.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking // Necesario para obtener el Flow de forma síncrona
import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 1. OBTENER EL TOKEN BLOQUEANDO EL HILO DE LA PETICIÓN
        // Usamos runBlocking para obtener el valor del Flow del DataStore de forma síncrona,
        // ya que los interceptores de OkHttp son síncronos.
        val token = runBlocking {
            authManager.getToken().first()
        }

        // 2. ADJUNTAR EL TOKEN SI EXISTE
        if (token != null) {
            Log.d("AuthInterceptor", "Adjuntando token JWT a la petición.")
            val newRequest = originalRequest.newBuilder()
                // Formato requerido por Spring Security
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }

        // Si no hay token, la petición pasa sin cabecera (útil para /login o /products)
        Log.d("AuthInterceptor", "Token no encontrado. Petición sin cabecera de Auth.")
        return chain.proceed(originalRequest)
    }
}