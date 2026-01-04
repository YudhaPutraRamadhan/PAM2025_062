package com.example.hobbyyk_new.view.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hobbyyk_new.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterViewModel = viewModel()
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.navigateToOtp) {
        viewModel.navigateToOtp?.let { emailUser ->
            navController.navigate("verify_otp/$emailUser")
            viewModel.navigateToOtp = null
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Daftar Akun",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFF6B35)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Header Section
            Text(
                text = "Ayo Bergabung!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF6B35)
            )
            Text(
                text = "Lengkapi data dirimu untuk mulai mengeksplorasi komunitas di Yogyakarta.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Username Field
            LabelText("Username")
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B35),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = Color(0xFFFF6B35),
                    cursorColor = Color(0xFFFF6B35)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            LabelText("Email")
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B35),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = Color(0xFFFF6B35),
                    cursorColor = Color(0xFFFF6B35)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            LabelText("Password")
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle",
                            tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B35),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = Color(0xFFFF6B35),
                    cursorColor = Color(0xFFFF6B35)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            LabelText("Konfirmasi Password")
            OutlinedTextField(
                value = viewModel.confPassword,
                onValueChange = { viewModel.confPassword = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35)
                    )
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B35),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = Color(0xFFFF6B35),
                    cursorColor = Color(0xFFFF6B35)
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Register Button
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !viewModel.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B35),
                    disabledContainerColor = Color(0xFFFF6B35).copy(alpha = 0.6f)
                )
            ) {
                if (viewModel.isLoading && !viewModel.showOtpDialog) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        "Daftar Sekarang",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LabelText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF424242),
        modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
    )
}