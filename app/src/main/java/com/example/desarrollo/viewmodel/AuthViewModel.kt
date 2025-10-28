package com.example.desarrollo.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desarrollo.model.UserCredentials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

    class AuthViewModel : ViewModel() {


        private val _loginName = MutableLiveData("")
        val loginName: LiveData<String> = _loginName

        private val _loginLastName = MutableLiveData("")
        val loginLastName: LiveData<String> = _loginLastName

        private val _loginEmail = MutableLiveData("")
        val loginEmail: LiveData<String> = _loginEmail

        private val _loginPassword = MutableLiveData("")
        val loginPassword: LiveData<String> = _loginPassword


        private val _loginNameError = MutableLiveData<String?>(null)
        val loginNameError: LiveData<String?> = _loginNameError

        private val _loginLastNameError = MutableLiveData<String?>(null)
        val loginLastNameError: LiveData<String?> = _loginLastNameError

        private val _loginEmailError = MutableLiveData<String?>(null)
        val loginEmailError: LiveData<String?> = _loginEmailError

        private val _loginPasswordError = MutableLiveData<String?>(null)
        val loginPasswordError: LiveData<String?> = _loginPasswordError

        private val _loginResult = MutableLiveData<Boolean?>(null)
        val loginResult: LiveData<Boolean?> = _loginResult


        private val _registerName = MutableLiveData("")
        val registerName: LiveData<String> = _registerName

        private val _registerLastName = MutableLiveData("")
        val registerLastName: LiveData<String> = _registerLastName

        private val _registerEmail = MutableLiveData("")
        val registerEmail: LiveData<String> = _registerEmail

        private val _registerPassword = MutableLiveData("")
        val registerPassword: LiveData<String> = _registerPassword

        private val _registerConfirmPassword = MutableLiveData("")
        val registerConfirmPassword: LiveData<String> = _registerConfirmPassword


        private val _registerNameError = MutableLiveData<String?>(null)
        val registerNameError: LiveData<String?> = _registerNameError

        private val _registerLastNameError = MutableLiveData<String?>(null)
        val registerLastNameError: LiveData<String?> = _registerLastNameError

        private val _registerEmailError = MutableLiveData<String?>(null)
        val registerEmailError: LiveData<String?> = _registerEmailError

        private val _registerPasswordError = MutableLiveData<String?>(null)
        val registerPasswordError: LiveData<String?> = _registerPasswordError

        private val _registerConfirmPasswordError = MutableLiveData<String?>(null)
        val registerConfirmPasswordError: LiveData<String?> = _registerConfirmPasswordError

        private val _registrationResult = MutableLiveData<Boolean?>(null)
        val registrationResult: LiveData<Boolean?> = _registrationResult


        private val registeredUsers = mutableListOf<UserCredentials>()

        init {

            registeredUsers.add(UserCredentials("Juan", "Perez", "juan@example.com", "password123"))
            registeredUsers.add(UserCredentials("Maria", "Gomez", "maria@example.com", "securepass"))
        }


        fun onLoginNameChanged(name: String) {
            _loginName.value = name
            _loginNameError.value = null
        }

        fun onLoginLastNameChanged(lastName: String) {
            _loginLastName.value = lastName
            _loginLastNameError.value = null
        }

        fun onLoginEmailChanged(email: String) {
            _loginEmail.value = email
            _loginEmailError.value = null
        }

        fun onLoginPasswordChanged(password: String) {
            _loginPassword.value = password
            _loginPasswordError.value = null
        }


        fun onRegisterNameChanged(name: String) {
            _registerName.value = name
            _registerNameError.value = null
        }

        fun onRegisterLastNameChanged(lastName: String) {
            _registerLastName.value = lastName
            _registerLastNameError.value = null
        }

        fun onRegisterEmailChanged(email: String) {
            _registerEmail.value = email
            _registerEmailError.value = null
        }

        fun onRegisterPasswordChanged(password: String) {
            _registerPassword.value = password
            _registerPasswordError.value = null
        }

        fun onRegisterConfirmPasswordChanged(confirmPassword: String) {
            _registerConfirmPassword.value = confirmPassword
            _registerConfirmPasswordError.value = null
        }


        private fun validateName(name: String): String? {
            return if (name.length < 3) "Escriba más de 3 caracteres" else null
        }

        private fun validateEmail(email: String): String? {
            return if (!email.contains("@")) "Se necesita este caracter @" else null
        }


        fun login() {
            _loginResult.value = null

            val name = _loginName.value ?: ""
            val lastName = _loginLastName.value ?: ""
            val email = _loginEmail.value ?: ""
            val password = _loginPassword.value ?: ""

            val nameError = validateName(name)
            val lastNameError = validateName(lastName)
            val emailError = validateEmail(email)

            _loginNameError.value = nameError
            _loginLastNameError.value = lastNameError
            _loginEmailError.value = emailError

            val hasErrors = nameError != null || lastNameError != null || emailError != null

            if (!hasErrors) {
                viewModelScope.launch {
                    delay(500)
                    val userFound = registeredUsers.any {
                        it.nombre == name && it.apellido == lastName && it.correo == email && it.contrasena == password
                    }
                    if (userFound) {
                        _loginResult.value = true
                    } else {
                        _loginPasswordError.value = "Contraseña o credenciales incorrectas"
                        _loginResult.value = false
                    }
                }
            }
        }


        fun register() {
            _registrationResult.value = null

            val name = _registerName.value ?: ""
            val lastName = _registerLastName.value ?: ""
            val email = _registerEmail.value ?: ""
            val password = _registerPassword.value ?: ""
            val confirmPassword = _registerConfirmPassword.value ?: ""

            val nameError = validateName(name)
            val lastNameError = validateName(lastName)
            val emailError = validateEmail(email)
            val passwordMatchError = if (password != confirmPassword) "Las contraseñas no coinciden" else null
            val passwordEmptyError = if (password.isBlank()) "La contraseña no puede estar vacía" else null
            val confirmPasswordEmptyError = if (confirmPassword.isBlank()) "Confirme su contraseña" else null


            _registerNameError.value = nameError
            _registerLastNameError.value = lastNameError
            _registerEmailError.value = emailError
            _registerPasswordError.value = passwordEmptyError
            _registerConfirmPasswordError.value = confirmPasswordEmptyError ?: passwordMatchError


            val hasErrors = nameError != null || lastNameError != null || emailError != null ||
                    passwordEmptyError != null || confirmPasswordEmptyError != null || passwordMatchError != null

            if (!hasErrors) {
                viewModelScope.launch {
                    delay(500) // Simular una llamada a la API
                    val userExists = registeredUsers.any { it.correo == email }
                    if (userExists) {
                        _registerEmailError.value = "Ya existe un usuario con este correo"
                        _registrationResult.value = false
                    } else {
                        registeredUsers.add(UserCredentials(name, lastName, email, password))
                        _registrationResult.value = true // Registro exitoso
                    }
                }
            }
        }

        fun resetLoginResult() {
            _loginResult.value = null
        }

        fun resetRegistrationResult() {
            _registrationResult.value = null
        }
    }
