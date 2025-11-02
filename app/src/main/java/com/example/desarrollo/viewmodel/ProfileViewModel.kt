package com.example.desarrollo.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(User(
        id = "1",
        username = "Martín Ejemplo",
        address = "Av. Siempre Viva 742",
        phoneNumber = "+56 9 1234 5678",
        profilePictureUri = ""
    ))
    val userProfile: StateFlow<User> = _userProfile.asStateFlow()

    val isSaving = mutableStateOf(false)
    val saveSuccess = mutableStateOf(false)

    fun onUsernameChange(newUsername: String) {
        _userProfile.update { it.copy(username = newUsername) }
    }

    fun onAddressChange(newAddress: String) {
        _userProfile.update { it.copy(address = newAddress) }
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        _userProfile.update { it.copy(phoneNumber = newPhoneNumber) }
    }

    fun onProfilePictureUriChange(uri: Uri?) {
        uri?.let {
            _userProfile.update { it.copy(profilePictureUri = uri.toString()) }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            isSaving.value = true
            saveSuccess.value = false
            kotlinx.coroutines.delay(1500)
            println("Perfil guardado: ${_userProfile.value}")
            isSaving.value = false
            saveSuccess.value = true
        }
    }
}

// Factory simple para crear el ProfileViewModel
class ProfileViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
