package com.example.desarrollo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.data.AuthManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager = AuthManager(application)

    /**
     * Estado que la MainActivity observará para decidir qué flujo mostrar (Auth o Main).
     * true = Sesión iniciada (mostrar MainFlow)
     * false = Sesión cerrada (mostrar AuthFlow)
     */
    val isLoggedIn: StateFlow<Boolean> = authManager.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Función de Login que se llama al tener éxito la autenticación
    fun setLoggedIn() {
        viewModelScope.launch {
            authManager.login()
        }
    }

    // Función de Logout que se llama desde ProfileScreen
    fun setLoggedOut() {
        viewModelScope.launch {
            authManager.logout()
        }
    }
}