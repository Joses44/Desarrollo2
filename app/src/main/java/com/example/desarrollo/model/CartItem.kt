package com.example.desarrollo.model



data class CartItem(
    val id: Long,
    val productId: Long,
    val quantity: Int
    // El servidor puede enviar más detalles aquí
)

// Archivo: CartRequest.kt (Payload para enviar al servidor)
data class CartRequest(
    val productId: Long,
    val quantity: Int
)