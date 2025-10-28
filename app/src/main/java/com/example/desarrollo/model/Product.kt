package com.example.desarrollo.model

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val unit: String,
    val stock: String,
    val imageRes: Int,
    val description: String,
    val rating: Double
)
