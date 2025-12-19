package com.example.desarrollo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.data.CartRepository
import com.example.desarrollo.data.CartState // Importar la clase de estado (Loading, Success, Error)
import com.example.desarrollo.model.CartItemDetails
import com.example.desarrollo.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 1. ESTADO DE LA UI (MODIFICADO)
data class CartUiState(
    val items: List<CartItemDetails> = listOf(),
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    // 2. FLUJO DE ESTADO (Mapea CartState a CartUiState)
    val uiState: StateFlow<CartUiState> = repository.cartState
        .map { state ->
            when (state) {
                is CartState.Loading -> CartUiState(isLoading = true)
                is CartState.Error -> CartUiState(errorMessage = state.message, isLoading = false)
                is CartState.Empty -> CartUiState(isLoading = false, total = 0.0)
                is CartState.Success -> {
                    CartUiState(
                        items = state.cartItems,
                        total = state.total,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState(isLoading = true)
        )

    init {
        fetchCart()
    }

    fun fetchCart() {
        viewModelScope.launch {
            repository.fetchCartDetails()
        }
    }

    // --- 3. FUNCIONES DE ACCIÓN ---

    /**
     * Añade un producto al carrito (cantidad +1). Ahora solo necesita el ID.
     */
    fun agregarAlCarrito(productId: Long) {
        viewModelScope.launch {
            // ✅ CORREGIDO: Usamos la instancia inyectada 'repository'
            repository.addOrAdjustItem(productId, 1)
        }
    }

    /**
     * Incrementa la cantidad de un ítem existente en 1 unidad.
     */
    fun incrementarUnidad(productId: Long) {
        viewModelScope.launch {
            repository.addOrAdjustItem(productId, 1)
        }
    }

    /**
     * Decrementa la cantidad de un ítem existente en 1 unidad.
     * Usa quantity = -1 para el endpoint de tu backend.
     */
    fun decrementarUnidad(productId: Long) {
        viewModelScope.launch {
            repository.addOrAdjustItem(productId, -1)
        }
    }

    // --- OTRAS FUNCIONES ---

    /**
     * Elimina un CartItem específico por su ID (DELETE /api/cart/{id}).
     */
    fun removerProducto(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeItem(cartItemId)
        }
    }

    /**
     * Vacía todo el carrito (DELETE /api/cart/clear).
     */
    fun vaciarCarrito() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}

// El CartViewModelFactory NO requiere cambios, ya que maneja la inyección manual.
class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}