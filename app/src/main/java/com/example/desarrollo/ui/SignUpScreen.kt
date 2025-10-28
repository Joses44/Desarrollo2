package com.example.desarrollo.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel = viewModel(),
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    val name by authViewModel.registerName.observeAsState("")
    val lastName by authViewModel.registerLastName.observeAsState("")
    val email by authViewModel.registerEmail.observeAsState("")
    val password by authViewModel.registerPassword.observeAsState("")
    val confirmPassword by authViewModel.registerConfirmPassword.observeAsState("")

    val nameError by authViewModel.registerNameError.observeAsState(null)
    val lastNameError by authViewModel.registerLastNameError.observeAsState(null)
    val emailError by authViewModel.registerEmailError.observeAsState(null)
    val passwordError by authViewModel.registerPasswordError.observeAsState(null)
    val confirmPasswordError by authViewModel.registerConfirmPasswordError.observeAsState(null)
    val registrationResult by authViewModel.registrationResult.observeAsState(null)

    // Observar el resultado del registro
    LaunchedEffect(registrationResult) {
        when (registrationResult) {
            true -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onRegistrationSuccess() // Navegar a la pantalla de login o directamente a la principal
                authViewModel.resetRegistrationResult()
            }
            false -> {
                // El error específico ya se muestra en el campo correspondiente (ej. email ya existe)
                Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
                authViewModel.resetRegistrationResult()
            }
            null -> { /* No hacer nada aún */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { authViewModel.onRegisterNameChanged(it) },
            label = { Text("Nombre") },
            isError = nameError != null,
            supportingText = { if (nameError != null) Text(nameError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { authViewModel.onRegisterLastNameChanged(it) },
            label = { Text("Apellido") },
            isError = lastNameError != null,
            supportingText = { if (lastNameError != null) Text(lastNameError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { authViewModel.onRegisterEmailChanged(it) },
            label = { Text("Correo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            supportingText = { if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { authViewModel.onRegisterPasswordChanged(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            supportingText = { if (passwordError != null) Text(passwordError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { authViewModel.onRegisterConfirmPasswordChanged(it) },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError != null,
            supportingText = { if (confirmPasswordError != null) Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.register() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    MaterialTheme {
        SignUpScreen(onRegistrationSuccess = {}, onNavigateToLogin = {})
    }
}