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
import coil.compose.AsyncImage
import com.example.desarrollo.R
import com.example.desarrollo.viewmodel.MainViewModel
import com.example.desarrollo.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val isSaving by profileViewModel.isSaving
    val saveSuccess by profileViewModel.saveSuccess
    
    // Recogemos el estado del modo oscuro del MainViewModel
    val useDarkMode by mainViewModel.isDarkMode.collectAsState()

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        profileViewModel.onProfilePictureUriChange(uri)
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

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                val hasPhoto = userProfile.profilePictureUri.isNotEmpty()

                if (hasPhoto) {
                    AsyncImage(
                        model = userProfile.profilePictureUri,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_default_profile_foreground),
                        contentDescription = "Foto predeterminada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

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

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value = userProfile.username, onValueChange = profileViewModel::onUsernameChange, label = { Text("Nombre de Usuario") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = userProfile.address, onValueChange = profileViewModel::onAddressChange, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = userProfile.phoneNumber, onValueChange = profileViewModel::onPhoneNumberChange, label = { Text("Número de Celular") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            Spacer(modifier = Modifier.height(24.dp))

            // --- Interruptor para Modo Oscuro ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = useDarkMode,
                    onCheckedChange = { mainViewModel.setDarkMode(it) }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = profileViewModel::saveProfile, enabled = !isSaving, modifier = Modifier.fillMaxWidth()) {
                if (isSaving) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar Cambios")
                }
            }

            if (saveSuccess) {
                Text(
                    "¡Perfil actualizado con éxito!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(3000)
                    profileViewModel.saveSuccess.value = false
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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