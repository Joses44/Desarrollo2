package com.example.desarrollo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para obtener el DataStore a nivel de aplicación
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class AuthManager(private val context: Context) {

    companion object {
        // 🔑 CLAVE PRINCIPAL: Almacenar la cadena JWT
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")

        // Clave para el modo oscuro (mantenida)
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    // =========================================================
    // GESTIÓN DEL TOKEN (REEMPLAZA IS_LOGGED_IN)
    // =========================================================

    /**
     * Guarda la cadena JWT recibida del backend.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    /**
     * Devuelve la cadena JWT guardada. El AuthInterceptor usará este método.
     * Retorna null si el usuario no está logueado.
     */
    fun getToken(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[AUTH_TOKEN] // Retorna la cadena o null
        }

    /**
     * Limpia el token al cerrar sesión.
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }

    // Un simple Flow para determinar si está logueado (si el token existe)
    val isLoggedIn: Flow<Boolean> = getToken().map { it != null }


    // --- Lógica del Modo Oscuro (Mantenida) ---
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false
        }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDark
        }
    }
}