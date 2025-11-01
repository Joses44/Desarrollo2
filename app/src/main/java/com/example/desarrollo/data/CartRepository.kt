package com.example.desarrollo.data

import com.example.desarrollo.model.CartItem
import com.example.desarrollo.model.CartItemDetails
import com.example.desarrollo.model.Product
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

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

    suspend fun removeItem(productId: Int) {
        cartDao.deleteItem(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun incrementItem(productId: Int) {
        val existingItem = cartDao.getCartItem(productId)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.insertItem(updatedItem)
        }
    }

    suspend fun decrementItem(productId: Int) {
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
