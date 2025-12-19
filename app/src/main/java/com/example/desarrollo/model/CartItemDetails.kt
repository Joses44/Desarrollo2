package com.example.desarrollo.model

data class CartItemDetails(
    val productId: Long, // 🎯 Cambiado a Long
    val name: String,
    val price: Int,
    val imageRes: Int,
    val unit: String,
    val quantity: Int
)