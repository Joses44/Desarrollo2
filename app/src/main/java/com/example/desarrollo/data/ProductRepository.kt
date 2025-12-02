package com.example.desarrollo.data

import com.example.desarrollo.model.CategoryWithProducts
import kotlinx.coroutines.flow.Flow
import com.example.desarrollo.model.Product // Importar el modelo de Producto
import com.example.desarrollo.network.ApiService // Importar la interfaz de red
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// El repositorio ahora necesita el DAO (local) y el servicio de API (remoto)
class ProductRepository(
    private val productDao: ProductDao,
    private val apiService: ApiService // <-- ¡Nueva dependencia de red!
) {

    // 1. Lógica de Lectura Local (Tu código original)
    // El ViewModel se suscribe a este Flow para obtener las actualizaciones
    val categoriesWithProducts: Flow<List<CategoryWithProducts>> = productDao.getCategoriesWithProducts()

    /**
     * Función para obtener productos del Backend a través de Retrofit.
     * Esta es la función clave que llama el ViewModel.
     */
    suspend fun fetchProductsFromApi(): List<Product> {
        // Aseguramos que la operación de red se ejecute en el hilo de IO
        return withContext(Dispatchers.IO) {
            // Llama al método GET /api/products en el backend
            apiService.getAllProducts()
        }
    }

    /**
     * Función para guardar los productos descargados en la base de datos local (Room).
     * Esto actualiza automáticamente el 'categoriesWithProducts' Flow.
     */
    suspend fun saveProducts(products: List<Product>) {
        withContext(Dispatchers.IO) {
            // 1. Limpieza
            productDao.clearAllProducts()

            // 2. Inserción: ¡CORREGIDO! Usamos 'insertProducts' que sí existe en el DAO.
            productDao.insertProducts(products) // ✅ Referencia resuelta
        }
    }
}
