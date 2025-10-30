package com.example.desarrollo.model

// Modelo de datos del usuario
data class User(
    val id: String = "",
    val username: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val profilePictureUri: String = ""
)