package com.example.desarrollo

import android.app.Application
import com.example.desarrollo.data.AppDatabase
import com.example.desarrollo.data.CartRepository
import com.example.desarrollo.data.ProductRepository
import com.example.desarrollo.data.AuthManager
import com.example.desarrollo.network.RetrofitClient
import com.example.desarrollo.viewmodel.CartViewModelFactory
class MyApplication : Application() {

    // 1. Base de Datos (Persistencia Local)
    private val database by lazy { AppDatabase.getDatabase(this) }

    // 2. AuthManager (Gestión de Token)
    val authManager by lazy { AuthManager(this) }

    // 3. ApiService (Red)
    private val apiService by lazy {
        RetrofitClient.getApiService(authManager)
    }

    // 4. Repositorio de Productos (Catálogo)
    val productRepository by lazy {
        ProductRepository(
            database.productDao(),
            apiService
        )
    }

    // 5. Repositorio de Carrito (¡CORREGIDO!)
    val cartRepository by lazy {
        CartRepository(

            apiService = apiService
        )
    }

    // 6. CartViewModelFactory (Necesario para la inyección manual del ViewModel)
    val cartViewModelFactory by lazy {
        // Necesitas que el Factory reciba el Repositorio
        CartViewModelFactory(cartRepository)
    }
}