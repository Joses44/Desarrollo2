package com.example.desarrollo.network

import com.example.desarrollo.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.Optional

interface ApiService {

    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product
}