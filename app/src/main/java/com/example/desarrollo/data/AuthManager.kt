package com.example.desarrollo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para obtener el DataStore a nivel de aplicación (solo una vez)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs") // Renombrado a user_prefs para más generalidad

class AuthManager(private val context: Context) {

    companion object {
        // Clave para el estado de inicio de sesión
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        // Clave para el modo oscuro
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    /**
     * Devuelve un Flow que emite el estado actual de la sesión.
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    /**
     * Guarda el estado como True (Sesión iniciada).
     */
    suspend fun login() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
        }
    }

    /**
     * Guarda el estado como False (Sesión cerrada).
     */
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
        }
    }

    // --- Lógica del Modo Oscuro ---

    /**
     * Devuelve un Flow que emite si el modo oscuro está activado.
     */
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false // Por defecto, el modo oscuro está desactivado
        }

    /**
     * Guarda la preferencia del modo oscuro del usuario.
     */
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDark
        }
    }
}
