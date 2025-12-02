package com.example.desarrollo.network

import com.example.desarrollo.model.Product
import retrofit2.Response // <-- ¡Importante!
import retrofit2.http.GET
import retrofit2.http.Path
// import java.util.Optional // Ya no es necesario si usas Response<T>

interface ApiService {

    @GET("products")
    // ✅ CORREGIDO: Ahora devuelve un objeto Response de Retrofit
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products/{id}")
    // ✅ CORREGIDO: Ahora devuelve un objeto Response de Retrofit
    suspend fun getProductById(@Path("id") id: Long): Response<Product>
}