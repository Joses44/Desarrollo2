package com.example.desarrollo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.desarrollo.model.CartItem
import com.example.desarrollo.model.CartItemDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("""
        SELECT
            p.id as productId,
            p.name as name,
            p.price as price,
            p.imageRes as imageRes,
            p.unit as unit,
            ci.quantity as quantity
        FROM cart_items ci
        INNER JOIN products p ON ci.productId = p.id
    """)
    fun getCartItems(): Flow<List<CartItemDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteItem(productId: Long) // 🎯 Cambiado a Long

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItem(productId: Long): CartItem? // 🎯 Cambiado a Long
}