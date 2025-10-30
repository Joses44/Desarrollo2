
package com.example.desarrollo.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // Estado para la pantalla de perfil
    private val _userProfile = MutableStateFlow(User(
        id = "1",
        username = "Martín Ejemplo",
        address = "Av. Siempre Viva 742",
        phoneNumber = "+56 9 1234 5678",
        profilePictureUri = ""
    ))
    val userProfile: StateFlow<User> = _userProfile.asStateFlow()

    // Estados para la UI (feedback al usuario)
    val isSaving = mutableStateOf(false)
    val saveSuccess = mutableStateOf(false)


    // --- Funciones para actualizar los campos ---

    fun onUsernameChange(newUsername: String) {
        _userProfile.update { it.copy(username = newUsername) }
    }

    fun onAddressChange(newAddress: String) {
        _userProfile.update { it.copy(address = newAddress) }
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        _userProfile.update { it.copy(phoneNumber = newPhoneNumber) }
    }

    // Función que se llama cuando se selecciona o captura una foto
    fun onProfilePictureUriChange(uri: Uri?) {
        uri?.let {
            _userProfile.update { it.copy(profilePictureUri = uri.toString()) }
        }
    }

    // --- Lógica de Guardado ---

    fun saveProfile() {
        viewModelScope.launch {
            isSaving.value = true
            saveSuccess.value = false

            // Simulación de la llamada a la base de datos o API
            kotlinx.coroutines.delay(1500)

            // Aquí iría el código real para persistir _userProfile.value
            println("Perfil guardado: ${_userProfile.value}")

            isSaving.value = false
            saveSuccess.value = true
        }
    }
}