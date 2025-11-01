package com.example.desarrollo.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.desarrollo.R
import com.example.desarrollo.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isSaving by viewModel.isSaving
    val saveSuccess by viewModel.saveSuccess

    // Lanzador para la Galería (Photo Picker)
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // Llama a la función del ViewModel para guardar la URI
        viewModel.onProfilePictureUriChange(uri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---  LÓGICA DE LA FOTO DE PERFIL (COMPLETADA) ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        // Al hacer clic, abre el selector de imagen (Galería)
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                // Si la URI está llena, Coil carga la imagen; si no, usa el recurso local
                val hasPhoto = userProfile.profilePictureUri.isNotEmpty()

                if (hasPhoto) {
                    AsyncImage(
                        model = userProfile.profilePictureUri,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Muestra la imagen predeterminada
                    Image(
                        painter = painterResource(id = R.drawable.ic_default_profile_foreground),
                        contentDescription = "Foto predeterminada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Ícono de Cámara para indicar que es clickable
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Cambiar foto",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            // ---  FIN DE LÓGICA DE LA FOTO DE PERFIL ---

            Spacer(modifier = Modifier.height(24.dp))

            // --- Campos de Edición (Tu código existente) ---
            OutlinedTextField(value = userProfile.username, onValueChange = viewModel::onUsernameChange, label = { Text("Nombre de Usuario") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = userProfile.address, onValueChange = viewModel::onAddressChange, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = userProfile.phoneNumber, onValueChange = viewModel::onPhoneNumberChange, label = { Text("Número de Celular") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            Spacer(modifier = Modifier.height(32.dp))

            // --- Botón de Guardar (Tu código existente) ---
            Button(onClick = viewModel::saveProfile, enabled = !isSaving, modifier = Modifier.fillMaxWidth()) {
                if (isSaving) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar Cambios")
                }
            }

            // Mostrar mensaje de éxito (Tu código existente)
            if (saveSuccess) {
                Text(
                    "¡Perfil actualizado con éxito!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.saveSuccess.value = false
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Cerrar Sesión
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}