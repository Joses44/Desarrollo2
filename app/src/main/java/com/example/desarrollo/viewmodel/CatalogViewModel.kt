package com.example.desarrollo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.data.ProductRepository
import com.example.desarrollo.model.CategoryWithProducts
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ----------------------------------------------------
// 1. DEFINICIÓN DEL ESTADO DE LA UI (PARA LA RED)
// ----------------------------------------------------
sealed interface ProductSyncState {
    object Loading : ProductSyncState // Carga inicial de la red
    object Success : ProductSyncState // Sincronización exitosa
    data class Error(val message: String) : ProductSyncState // Fallo de red (Backend inactivo)
}

class CatalogViewModel(private val repository: ProductRepository) : ViewModel() {

    // 2. LIVE DATA / STATEFLOW DE LOS DATOS DE ROOM (La fuente de verdad)
    // El ViewModel expone los datos de la BD local (Room).
    val categoriesWithProducts: StateFlow<List<CategoryWithProducts>> = repository.categoriesWithProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 3. ESTADO DE LA SINCRONIZACIÓN DE LA RED
    // Exponemos el estado de la última llamada a la API.
    private val _syncState = repository.syncState // Asumimos que el repositorio expone este estado
    val syncState: StateFlow<ProductSyncState> = _syncState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ProductSyncState.Loading
    )

    // 4. COMBINACIÓN DE ESTADOS (Opcional, pero útil si necesitas una sola UiState)
    // Aquí se combina el estado de la red (syncState) y los datos de la BD.
    // val uiState: StateFlow<CombinedCatalogState> = combine(categoriesWithProducts, syncState) { data, sync ->
    //     CombinedCatalogState(data = data, sync = sync)
    // }.stateIn( ... )

    // 5. INICIAR LA SINCRONIZACIÓN
    init {
        // Al crear el ViewModel, intenta obtener los productos del backend
        // Si tiene éxito, Room se actualiza automáticamente (triggering categoriesWithProducts).
        // Si falla (backend caído), _syncState se actualiza a Error.
        refreshProducts()
    }

    fun refreshProducts() {
        viewModelScope.launch {
            repository.refreshProductsFromNetwork() // Llamada que ejecuta la lógica de red
        }
    }
}

// ----------------------------------------------------
// 6. FACTORY (Se mantiene igual)
// ----------------------------------------------------
class CatalogViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
