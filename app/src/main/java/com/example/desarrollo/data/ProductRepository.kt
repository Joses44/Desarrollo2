package com.example.desarrollo.data

import com.example.desarrollo.model.CategoryWithProducts
import com.example.desarrollo.network.ApiService
import com.example.desarrollo.viewmodel.ProductSyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response

// El repositorio ahora necesita el DAO (local) y el servicio de API (remoto)
class ProductRepository(
    private val productDao: ProductDao,
    private val apiService: ApiService
) {

    // 1. ESTADO DE SINCRONIZACIÓN (Nuevo)
    private val _syncState = MutableStateFlow<ProductSyncState>(ProductSyncState.Loading)
    // El ViewModel se suscribe a este Flow para saber si la red está OK
    val syncState: StateFlow<ProductSyncState> = _syncState.asStateFlow()

    // 2. Lógica de Lectura Local (Room)
    val categoriesWithProducts: Flow<List<CategoryWithProducts>> = productDao.getCategoriesWithProducts()

    /**
     * Función principal para orquestar la llamada a la red y guardar en Room.
     * Esta función es llamada desde el ViewModel.
     */
    suspend fun refreshProductsFromNetwork() {
        // Establece el estado de carga solo si el estado actual no es ya de éxito/error
        if (_syncState.value != ProductSyncState.Success) {
            _syncState.value = ProductSyncState.Loading
        }

        withContext(Dispatchers.IO) {
            try {
                // 1. Llama a la API
                val response = apiService.getAllProducts()

                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()!!

                    // 2. Guardar en Room
                    productDao.clearAllProducts()
                    productDao.insertProducts(products)

                    // 3. Éxito: Notifica al ViewModel que todo está bien
                    _syncState.value = ProductSyncState.Success

                } else {
                    // Fallo HTTP (ej. 401, 404, 500)
                    val errorMsg = "Error del servidor: ${response.code()} ${response.message()}"
                    _syncState.value = ProductSyncState.Error(errorMsg)
                }
            } catch (e: Exception) {
                // Fallo de conexión (Servidor OFFLINE, sin internet)
                // 4. Fallo: Notifica al ViewModel el error de conexión
                _syncState.value = ProductSyncState.Error("Fallo de conexión. ¿El servidor está activo?")
            }
        }
    }


}


