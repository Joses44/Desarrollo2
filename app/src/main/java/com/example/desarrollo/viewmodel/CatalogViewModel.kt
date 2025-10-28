package com.example.desarrollo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.desarrollo.model.Category
import com.example.desarrollo.model.Product
import com.example.desarrollo.model.SampleData

class CatalogViewModel : ViewModel() {

    // Exponemos la lista de categorías desde SampleData
    val categories: List<Category> = SampleData.categories

    // Lista de todos los productos planos (opcional)
    val allProducts: List<Product> by lazy {
        categories.flatMap { it.products }
    }

    init {
        Log.d("CatalogViewModel", "Categories loaded: $categories")
    }

    // Función para obtener productos por categoría
    fun getProductsByCategory(categoryName: String): List<Product> {
        return categories.find { it.name == categoryName }?.products ?: emptyList()
    }
}
