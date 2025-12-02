package com.example.desarrollo

import android.app.Application
import com.example.desarrollo.data.AppDatabase
import com.example.desarrollo.data.CartRepository
import com.example.desarrollo.data.ProductRepository
import com.example.desarrollo.data.AuthManager // 🔑 Para manejar el token JWT
import com.example.desarrollo.network.RetrofitClient // 📞 Para crear la ApiService

class MyApplication : Application() {

    // 1. Base de Datos (Persistencia Local)
    private val database by lazy { AppDatabase.getDatabase(this) }

    // 2. AuthManager (Gestión de Token)
    // Inicializa el administrador de autenticación, que usa SharedPreferences/DataStore.
    val authManager by lazy { AuthManager(this) }

    // 3. ApiService (Red)
    // Inicializa el cliente Retrofit, pasándole el AuthManager para que configure el Interceptor.
    private val apiService by lazy {
        // RetrofitClient debe tener un método que reciba el AuthManager
        RetrofitClient.getApiService(authManager)
    }

    // 4. Repositorio de Productos (Catálogo)
    // Ahora inyectamos el ProductDao (local) y la ApiService (remoto)
    val productRepository by lazy {
        ProductRepository(
            database.productDao(), // DAO
            apiService // 📞 Referencia a la red (resuelve el TODO)
        )
    }

    // 5. Repositorio de Carrito (Si necesita lógica de red, también inyecta apiService)
    val cartRepository by lazy {
        CartRepository(
            database.cartDao(),
            apiService // Inyecta la dependencia de red aquí también
        )
    }

}
