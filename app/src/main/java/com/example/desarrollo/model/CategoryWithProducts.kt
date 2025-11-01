package com.example.desarrollo.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithProducts(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "name",
        entityColumn = "categoryId"
    )
    val products: List<Product>
)
