package com.example.hobbyyk_new.view.screen.user

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hobbyyk_new.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyChangeEmailScreen(navController: NavController, newEmail: String) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.message) {
        viewModel.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Verifikasi Email") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Verifikasi Email Baru", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Kode OTP dikirim ke: $newEmail", color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otp, onValueChange = { if(it.length <= 6) otp = it },
                label = { Text("Kode OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.verifyChangeEmail(otp, newEmail) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = otp.length >= 4
            ) {
                if (viewModel.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Verifikasi Email")
            }
        }
    }
}