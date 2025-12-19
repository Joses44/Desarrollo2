package com.example.desarrollo.data

import com.example.desarrollo.model.CartItem
import com.example.desarrollo.model.CartItemDetails
import com.example.desarrollo.model.Product
import kotlinx.coroutines.flow.Flow
import com.example.desarrollo.network.ApiService

class CartRepository(private val cartDao: CartDao) { // Eliminado parámetro no usado

    val cartItems: Flow<List<CartItemDetails>> = cartDao.getCartItems()

    suspend fun addItem(product: Product) {
        val existingItem = cartDao.getCartItem(product.id)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.insertItem(updatedItem)
        } else {
            cartDao.insertItem(CartItem(productId = product.id, quantity = 1))
        }
    }

    // 🎯 CORRECCIÓN: Cambiado de Int a Long
    suspend fun removeItem(productId: Long) {
        cartDao.deleteItem(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    // 🎯 CORRECCIÓN: Cambiado de Int a Long
    suspend fun incrementItem(productId: Long) {
        val existingItem = cartDao.getCartItem(productId)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.insertItem(updatedItem)
        }
    }

    // 🎯 CORRECCIÓN: Cambiado de Int a Long
    suspend fun decrementItem(productId: Long) {
        val existingItem = cartDao.getCartItem(productId)
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity - 1)
                cartDao.insertItem(updatedItem)
            } else {
                cartDao.deleteItem(productId)
            }
        }
    }
}