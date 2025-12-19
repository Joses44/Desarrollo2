package com.example.desarrollo.data

import android.util.Log
import com.example.desarrollo.model.CategoryWithProducts
import com.example.desarrollo.model.SampleData
import com.example.desarrollo.network.ApiService
import com.example.desarrollo.viewmodel.ProductSyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productDao: ProductDao,
    private val apiService: ApiService
) {
    private val _syncState = MutableStateFlow<ProductSyncState>(ProductSyncState.Loading)
    val syncState: StateFlow<ProductSyncState> = _syncState.asStateFlow()

    val categoriesWithProducts: Flow<List<CategoryWithProducts>> = productDao.getCategoriesWithProducts()

    suspend fun refreshProductsFromNetwork() {
        _syncState.value = ProductSyncState.Loading

        withContext(Dispatchers.IO) {
            try {
                // 1. Asegurar que las categorías existan en la BD para que el JOIN funcione
                productDao.insertCategories(SampleData.categories)

                val response = apiService.getAllProducts()

                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    Log.d("ProductRepository", "Productos recibidos del server: ${products.size}")

                    if (products.isNotEmpty()) {
                        productDao.deleteAndInsertProducts(products)
                        _syncState.value = ProductSyncState.Success
                    } else {
                        Log.w("ProductRepository", "Server vacío. Cargando SampleData...")
                        loadSampleData()
                    }
                } else {
                    Log.e("ProductRepository", "Error API: ${response.code()}. Cargando locales...")
                    loadSampleData()
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "Fallo de red: ${e.message}. Usando modo offline.")
                loadSampleData()
            }
        }
    }

    /**
     * Carga los datos de prueba cuando el servidor no está disponible.
     */
    private suspend fun loadSampleData() {
        productDao.deleteAndInsertProducts(SampleData.products)
        _syncState.value = ProductSyncState.Success
    }
}