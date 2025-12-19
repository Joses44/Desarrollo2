package com.example.desarrollo.network

import com.example.desarrollo.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.desarrollo.model.CartItem
import com.example.desarrollo.model.CartRequest

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

    @GET("cart") // Mapea a /api/cart
    suspend fun getCartItems(): Response<List<CartItem>>

    /**
     * 2. GET /api/cart/total: Obtener el precio total del carrito.
     * Devuelve el precio total como Double.
     */
    @GET("cart/total") // Mapea a /api/cart/total
    suspend fun getCartTotal(): Response<Double>

    /**
     * 3. POST /api/cart/add: Añadir/Actualizar ítem en el carrito.
     * Usa CartRequest como cuerpo de la solicitud.
     */
    @POST("cart/add") // Mapea a /api/cart/add
    suspend fun addOrUpdateItemInCart(@Body request: CartRequest): Response<CartItem>
    // NOTA: Usamos CartItem como respuesta, ya que es lo que devuelve el backend en caso de éxito (HTTP 201)

    /**
     * 4. DELETE /api/cart/{id}: Eliminar un CartItem específico por su ID.
     * El {id} es el ID del CartItem (la línea de carrito), no del Product.
     */
    @DELETE("cart/{id}") // Mapea a /api/cart/{id}
    suspend fun removeCartItem(@Path("id") cartItemId: Long): Response<Unit>

    /**
     * 5. DELETE /api/cart/clear: Vaciar todo el carrito.
     * Devuelve HTTP 204 No Content (Response<Unit>).
     */
    @DELETE("cart/clear") // Mapea a /api/cart/clear
    suspend fun clearCart(): Response<Unit>
}