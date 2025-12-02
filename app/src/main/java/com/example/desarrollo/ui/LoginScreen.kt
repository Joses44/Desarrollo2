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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desarrollo.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    // 💡 CORRECCIÓN PRINCIPAL: onLoginSuccess ahora acepta el token (String)
    onLoginSuccess: (token: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current

    // Observación de estados del ViewModel
    val email by authViewModel.loginEmail.observeAsState("")
    val password by authViewModel.loginPassword.observeAsState("")
    val emailError by authViewModel.loginEmailError.observeAsState(null)
    val passwordError by authViewModel.loginPasswordError.observeAsState(null)

    // Resultados de la operación
    val loginResult by authViewModel.loginResult.observeAsState(null)

    // ⚠️ SUPOSICIÓN: Se asume que AuthViewModel tiene un LiveData o StateFlow para exponer el token exitoso
    // Si usas un LiveData/StateFlow distinto en tu AuthViewModel, cámbialo aquí.
    val successfulToken by authViewModel.successfulToken.observeAsState(null)

    LaunchedEffect(loginResult) {
        when (loginResult) {
            true -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                // 🚀 LÓGICA CORREGIDA: Pasar el token al callback
                successfulToken?.let { token ->
                    // Llamamos al callback y pasamos el token al AuthNavigation
                    onLoginSuccess(token)
                } ?: run {
                    // Esto indica que el login fue true, pero el token no se pudo obtener (error de lógica interna)
                    Toast.makeText(context, "Error interno: Token no disponible", Toast.LENGTH_LONG).show()
                }

                authViewModel.resetLoginResult()
            }
            false -> {
                Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                authViewModel.resetLoginResult()
            }
            null -> { /* Sin acción */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 32.dp))

        // Campo de Correo
        OutlinedTextField(
            value = email,
            onValueChange = { authViewModel.onLoginEmailChanged(it) },
            label = { Text("Correo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            supportingText = { if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { authViewModel.onLoginPasswordChanged(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            supportingText = { if (passwordError != null) Text(passwordError!!, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón Ingresar
        Button(
            onClick = { authViewModel.login() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Botón Registro
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}