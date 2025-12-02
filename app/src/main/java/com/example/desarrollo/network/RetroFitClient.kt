package com.example.desarrollo.network

import com.example.desarrollo.data.AuthManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    /**
     * Construye y devuelve la implementación de la ApiService.
     * Recibe AuthManager para el futuro Interceptor.
     */
    fun getApiService(authManager: AuthManager): ApiService {

        // Interceptor para ver las peticiones/respuestas en Logcat
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Cliente OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // Añadimos nuestro interceptor (AuthInterceptor) aquí.
            .addInterceptor(AuthInterceptor(authManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        // Instancia de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}