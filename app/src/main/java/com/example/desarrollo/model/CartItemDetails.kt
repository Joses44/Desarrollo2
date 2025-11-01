package com.example.desarrollo.model

/**
 * Clase de datos que une la información de un Producto con la cantidad en el carrito.
 * Room usará esta clase para devolver el resultado de una consulta JOIN.
 */
data class CartItemDetails(
    val productId: Int,
    val name: String,
    val price: Int,
    val imageRes: Int,
    val unit: String,
    val quantity: Int
)
