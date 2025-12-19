package com.example.desarrollo.data

import com.example.desarrollo.model.CartItemDetails
import com.example.desarrollo.model.CartRequest // Tu payload para POST /add
import com.example.desarrollo.network.ApiService // Tu interfaz de Retrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.desarrollo.data.CartState
import com.example.desarrollo.model.CartItem

class CartRepository(
    // Dejamos el DAO por si lo usas para persistencia/caché, pero el backend es la fuente principal
    // ¡NUEVA DEPENDENCIA CLAVE!
    private val apiService: ApiService
) {

    // --- NUEVA GESTIÓN DE ESTADO (StateFlow para la UI) ---
    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    // --- FUNCIONES DEL BACKEND ---

    /**
     * 1. Carga inicial del carrito y actualización del estado.
     */
    suspend fun fetchCartDetails() {
        _cartState.value = CartState.Loading
        try {
            // Llama a los dos endpoints para obtener ítems y total
            val itemsResponse = apiService.getCartItems()
            val totalResponse = apiService.getCartTotal()

            if (itemsResponse.isSuccessful && totalResponse.isSuccessful) {
                val items = itemsResponse.body() ?: emptyList()
                val total = totalResponse.body() ?: 0.0

                if (items.isEmpty()) {
                    _cartState.value = CartState.Empty
                } else {
                    // **NOTA DE MAPEO:** Aquí debes convertir List<CartItem> (lo que viene del backend)
                    // a List<CartItemDetails> (lo que quiere tu UI).
                    // Asumiendo que existe una función de extensión mapToDetails()
                    // val detailedItems = items.map { it.mapToDetails() }

                    // Usamos una lista vacía de ejemplo temporal:
                    val detailedItems = emptyList<CartItemDetails>()

                    _cartState.value = CartState.Success(detailedItems, total)
                }
            } else {
                val errorMsg = itemsResponse.errorBody()?.string() ?: "Error desconocido"
                _cartState.value = CartState.Error(errorMsg)
            }
        } catch (e: Exception) {
            _cartState.value = CartState.Error("Error de red: ${e.message}")
        }
    }

    /**
     * 2. Añade/Ajusta la cantidad (incrementar, decrementar o añadir nuevo).
     * Mapea al endpoint POST /api/cart/add que espera productId y quantity (+1, -1, etc.).
     */
    suspend fun addOrAdjustItem(productId: Long, quantity: Int) {
        try {
            val request = CartRequest(productId, quantity)
            val response = apiService.addOrUpdateItemInCart(request) // Asumo que esta función existe en ApiService

            if (response.isSuccessful) {
                fetchCartDetails() // Recarga el carrito completo para actualizar la UI
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error al modificar ítem."
                _cartState.value = CartState.Error(errorBody)
                fetchCartDetails() // Recargar para limpiar el error visual si es posible
            }
        } catch (e: Exception) {
            _cartState.value = CartState.Error("Error de red al ajustar ítem: ${e.message}")
        }
    }

    /**
     * 3. Elimina un CartItem específico por su ID. Mapea a DELETE /api/cart/{id}.
     */
    suspend fun removeItem(cartItemId: Long) {
        try {
            val response = apiService.removeCartItem(cartItemId)
            if (response.isSuccessful) {
                fetchCartDetails() // Recargar el carrito
            } else {
                // Manejo de errores...
                fetchCartDetails()
            }
        } catch (e: Exception) {
            _cartState.value = CartState.Error("Error de red al eliminar ítem: ${e.message}")
        }
    }

    /**
     * 4. Vacía todo el carrito. Mapea a DELETE /api/cart/clear.
     */
    suspend fun clearCart() {
        try {
            val response = apiService.clearCart()
            if (response.isSuccessful) {
                _cartState.value = CartState.Empty // Asumimos que la operación es definitiva
            } else {
                // Manejo de errores...
            }
        } catch (e: Exception) {
            _cartState.value = CartState.Error("Error de red al vaciar carrito: ${e.message}")
        }
    }


}