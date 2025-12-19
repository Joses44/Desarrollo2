package com.example.desarrollo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: Long, // 🎯 Ahora tiene ID numérico para relacionarse con Product
    val name: String,
    val iconRes: Int = 0 // Añadido para que coincida con SampleData
)