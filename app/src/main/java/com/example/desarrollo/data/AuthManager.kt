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
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthManager(private val context: Context) {

    // Clave para almacenar el estado de inicio de sesión (booleano)
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    /**
     * Devuelve un Flow que emite el estado actual de la sesión.
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Si la clave existe, devuelve su valor; si no, devuelve false (sesión no iniciada)
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
}