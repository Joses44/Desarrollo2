package com.example.desarrollo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.data.ProductRepository
import com.example.desarrollo.model.CategoryWithProducts
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 1. DEFINICIÓN DEL ESTADO DE LA UI PARA LA RED
 * Maneja los estados de la llamada al API de Spring Boot.
 */
sealed interface ProductSyncState {
    object Loading : ProductSyncState
    object Success : ProductSyncState
    data class Error(val message: String) : ProductSyncState
}

/**
 * 2. VIEWMODEL DEL CATÁLOGO
 * Utiliza el ProductRepository para obtener datos de Room y sincronizar con el Backend.
 */
class CatalogViewModel(private val repository: ProductRepository) : ViewModel() {

    /**
     * Fuente de verdad: Datos provenientes de la base de datos local (Room).
     * Se actualiza automáticamente cuando el repositorio inserta nuevos datos.
     */
    val categoriesWithProducts: StateFlow<List<CategoryWithProducts>> = repository.categoriesWithProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Estado de sincronización: Refleja si el Backend respondió correctamente.
     */
    val syncState: StateFlow<ProductSyncState> = repository.syncState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ProductSyncState.Loading
        )

    init {
        // Al iniciar la App, intentamos traer productos frescos del servidor
        refreshProducts()
    }

    /**
     * Llama al repositorio para descargar los productos de la API.
     */
    fun refreshProducts() {
        viewModelScope.launch {
            repository.refreshProductsFromNetwork()
        }
    }
}

/**
 * 3. FACTORY PARA EL VIEWMODEL
 * Necesario porque pasamos el ProductRepository como parámetro al constructor.
 */
class CatalogViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}