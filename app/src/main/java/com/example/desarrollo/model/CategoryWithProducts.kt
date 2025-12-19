package com.example.desarrollo.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithProducts(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",    // 🎯 Antes era "name", ahora usamos el ID Long
        entityColumn = "categoryId" // 🎯 Se une con el categoryId del Producto
    )
    val products: List<Product>
)