package com.example.desarrollo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.data.CartRepository
import com.example.desarrollo.model.CartItemDetails
import com.example.desarrollo.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItemDetails> = listOf(),
    val total: Int = 0
)

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val uiState: StateFlow<CartUiState> = repository.cartItems
        .map { cartItems ->
            val total = cartItems.sumOf { it.price * it.quantity }
            CartUiState(items = cartItems, total = total)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState()
        )

    fun agregarAlCarrito(producto: Product) {
        viewModelScope.launch {
            repository.addItem(producto)
        }
    }

    fun removerProducto(productId: Long) { // ✅ Usando Long
        viewModelScope.launch {
            repository.removeItem(productId)
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun incrementarUnidad(productId: Long) { // ✅ Usando Long
        viewModelScope.launch {
            repository.incrementItem(productId)
        }
    }

    fun decrementarUnidad(productId: Long) { // ✅ Usando Long
        viewModelScope.launch {
            repository.decrementItem(productId)
        }
    }
}

class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}