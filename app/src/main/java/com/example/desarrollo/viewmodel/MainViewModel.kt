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

    // 💡 NOTA: Idealmente, el AuthManager debería ser inyectado desde MyApplication,
    // pero funciona así si no quieres cambiar la estructura de la Factory.
    private val authManager = AuthManager(application)

    val isLoggedIn: StateFlow<Boolean> = authManager.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // --- Lógica de Modo Oscuro ---
    val isDarkMode: StateFlow<Boolean> = authManager.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // =========================================================
    // 🆕 FUNCIONES DE SESIÓN ACTUALIZADAS (USA saveToken/clearToken)
    // =========================================================

    /**
     * Reemplaza a login(). Usamos una cadena placeholder por ahora.
     * En una app real, llamarías a esta función después de un POST exitoso a /api/auth/login.
     */
    fun setLoggedIn(token: String) { // Ahora debe aceptar el token
        viewModelScope.launch {
            authManager.saveToken(token) // Usa la función correcta
        }
    }

    /**
     * Reemplaza a logout(). Limpia el token.
     */
    fun setLoggedOut() {
        viewModelScope.launch {
            authManager.clearToken() // Usa la función correcta
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            authManager.setDarkMode(isDark)
        }
    }
}