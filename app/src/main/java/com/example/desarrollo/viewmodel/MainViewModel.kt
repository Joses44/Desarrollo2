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

    fun setLoggedIn() {
        viewModelScope.launch {
            authManager.login()
        }
    }

    fun setLoggedOut() {
        viewModelScope.launch {
            authManager.logout()
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            authManager.setDarkMode(isDark)
        }
    }
}
