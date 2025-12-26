package com.example.hobbyyk_new.view.screen.user

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hobbyyk_new.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyChangePassScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current

    var otp by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confPass by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.message) {
        viewModel.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Verifikasi Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Langkah Terakhir", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Masukkan Kode OTP dari email dan password baru Anda.", color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = otp, onValueChange = { if(it.length <= 6) otp = it },
                label = { Text("Kode OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPass, onValueChange = { newPass = it },
                label = { Text("Password Baru") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confPass, onValueChange = { confPass = it },
                label = { Text("Konfirmasi Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.verifyChangePass(otp, newPass, confPass) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = otp.isNotEmpty() && newPass.isNotEmpty()
            ) {
                if (viewModel.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Ubah Password")
            }
        }
    }
}