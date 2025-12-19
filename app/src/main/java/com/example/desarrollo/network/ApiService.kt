package com.example.desarrollo.network

import com.example.desarrollo.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    /**
     * Obtiene la lista completa de productos.
     * Acceso: Público (según nuestra WebSecurityConfig)
     */
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>

    /**
     * Obtiene un producto específico por su ID.
     * Acceso: Público
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Response<Product>

    /**
     * Crea un nuevo producto.
     * Acceso: Requiere Token JWT
     */
    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    /**
     * Actualiza un producto existente.
     * Acceso: Requiere Token JWT
     */
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body product: Product
    ): Response<Product>

    /**
     * Elimina un producto.
     * Acceso: Requiere Token JWT (Solo ADMIN)
     */
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>
}