package com.example.desarrollo.data

import com.example.desarrollo.model.CartItemDetails // Necesario para el caso Success

/**
 * Estado que representa el resultado de la operación del carrito (backend).
 * Esto separa la lógica de red de la lógica de presentación (UI).
 */
sealed class CartState {
    data object Loading : CartState() // La solicitud está en curso

    // La solicitud fue exitosa, contiene los ítems detallados para la UI y el total
    data class Success(val cartItems: List<CartItemDetails>, val total: Double) : CartState()

    data class Error(val message: String) : CartState() // Ocurrió un error (red o servidor)

    data object Empty : CartState() // El carrito está vacío (respuesta 200 con lista vacía)
}