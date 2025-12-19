package com.example.desarrollo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.desarrollo.model.Category
import com.example.desarrollo.model.CategoryWithProducts
import com.example.desarrollo.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithProducts(): Flow<List<CategoryWithProducts>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    @Query("DELETE FROM products")
    suspend fun clearAllProducts()

    /**
     * Sincronización atómica: Limpia y guarda los nuevos productos del Backend.
     */
    @Transaction
    suspend fun deleteAndInsertProducts(products: List<Product>) {
        clearAllProducts()
        insertProducts(products)
    }
}