package com.example.desarrollo.ui


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current


    val email by authViewModel.loginEmail.observeAsState("")
    val password by authViewModel.loginPassword.observeAsState("")

    val nameError by authViewModel.loginNameError.observeAsState(null)
    val lastNameError by authViewModel.loginLastNameError.observeAsState(null)
    val emailError by authViewModel.loginEmailError.observeAsState(null)
    val passwordError by authViewModel.loginPasswordError.observeAsState(null)
    val loginResult by authViewModel.loginResult.observeAsState(null)


    LaunchedEffect(loginResult) {
        when (loginResult) {
            true -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                onLoginSuccess() // Navegar a la pantalla principal
                authViewModel.resetLoginResult()
            }
            false -> {
                Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                authViewModel.resetLoginResult()
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
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 32.dp))


        Spacer(modifier = Modifier.height(8.dp))


        Spacer(modifier = Modifier.height(8.dp))

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

        Button(
            onClick = { authViewModel.login() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MaterialTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
    }
}