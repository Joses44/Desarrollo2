package com.example.desarrollo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Int,
    val unit: String,
    val stock: Int,
    val imageRes: Int,
    val description: String,
    val rating: Double,
    val categoryId: String, // Llave foránea para la categoría
    var cantidad: Int = 0
)
