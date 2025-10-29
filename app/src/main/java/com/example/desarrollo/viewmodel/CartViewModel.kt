package com.example.desarrollo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.desarrollo.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Clase de estado para la UI del carrito.
 * Contiene la lista de productos y el total.
 * Es una 'data class' para que StateFlow pueda compararla eficientemente.
 */
data class CartUiState(
    val productosEnCarrito: List<Product> = listOf(),
    val total: Double = 0.0
)

class CartViewModel : ViewModel() {

    // 1. _uiState es PRIVADO y MUTABLE. Es la fuente de la verdad.
    private val _uiState = MutableStateFlow(CartUiState())

    // 2. uiState es PÚBLICO e INMUTABLE. La UI observará este flujo.
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    /**
     * Añade una unidad de un producto al carrito.
     */
    fun agregarAlCarrito(producto: Product) {
        // 'update' es una forma segura de modificar el estado.
        _uiState.update { currentState ->
            val productos = currentState.productosEnCarrito.toMutableList()
            val productoExistente = productos.find { it.id == producto.id }

            if (productoExistente != null) {
                // Creamos una NUEVA instancia del producto con la cantidad actualizada.
                val productoActualizado = productoExistente.copy(cantidad = productoExistente.cantidad + 1)
                val index = productos.indexOf(productoExistente)
                productos[index] = productoActualizado
            } else {
                // Creamos una NUEVA instancia con cantidad 1.
                productos.add(producto.copy(cantidad = 1))
            }
            // Devolvemos un NUEVO estado con la lista actualizada
            currentState.copy(productosEnCarrito = productos)
        }
        recalcularTotal()
    }

    /**
     * Remueve una unidad de un producto del carrito.
     */
    fun removerUnidad(producto: Product) {
        _uiState.update { currentState ->
            val productos = currentState.productosEnCarrito.toMutableList()
            val productoExistente = productos.find { it.id == producto.id }

            if (productoExistente != null) {
                if (productoExistente.cantidad > 1) {
                    // Creamos una NUEVA instancia con la cantidad actualizada.
                    val productoActualizado = productoExistente.copy(cantidad = productoExistente.cantidad - 1)
                    val index = productos.indexOf(productoExistente)
                    productos[index] = productoActualizado
                } else {
                    // Si la cantidad es 1, simplemente eliminamos el producto de la lista.
                    productos.remove(productoExistente)
                }
            }
            // Devolvemos un NUEVO estado con la lista actualizada
            currentState.copy(productosEnCarrito = productos)
        }
        recalcularTotal()
    }

    /**
     * Elimina todos los productos del carrito.
     */
    fun vaciarCarrito() {
        _uiState.update {
            //se devuelve a un estado nuevo y vacío.
            CartUiState()
        }
    }

    /**
     * Recalcula el total y actualiza el estado.
     * se llama cada vez que modificamos el carrito.
     */
    private fun recalcularTotal() {
        _uiState.update { currentState ->
            val nuevoTotal = currentState.productosEnCarrito.sumOf { it.price * it.cantidad.toDouble() }
            currentState.copy(total = nuevoTotal)
        }
    }
}
