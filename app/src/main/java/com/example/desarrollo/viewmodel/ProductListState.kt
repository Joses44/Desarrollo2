package com.example.desarrollo.viewmodel
import com.example.desarrollo.model.Product
sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState() // Asumimos una lista plana de productos
    data class Error(val message: String) : ProductListState()
}