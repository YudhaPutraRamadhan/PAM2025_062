package com.example.hobbyyk_new.view.screen.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hobbyyk_new.utils.Constants
import com.example.hobbyyk_new.view.screen.auth.LabelText
import com.example.hobbyyk_new.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    var username by remember { mutableStateOf ("") }
    var bio by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) { viewModel.fetchProfile() }
    LaunchedEffect(viewModel.userProfile) {
        viewModel.userProfile?.let {
            if (username.isEmpty()) username = it.username
            if (bio.isEmpty()) bio = it.bio ?: ""
            if (noHp.isEmpty()) noHp = it.no_hp ?: ""
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFF1A1A1A))
                    }
                }
            )
        }
    ) { paddingValues ->
        if (viewModel.isLoading && viewModel.userProfile == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF6B35))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.clickable { imageLauncher.launch("image/*") }
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF5F5F5))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(selectedImageUri ?: "${Constants.URL_GAMBAR_BASE}${viewModel.userProfile?.profile_pic}")
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                    Surface(
                        modifier = Modifier.size(40.dp).shadow(4.dp, CircleShape),
                        color = Color(0xFFFF6B35),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.padding(10.dp).size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Column(Modifier.fillMaxWidth()) {
                    LabelText("Username")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFFFF6B35)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF6B35),
                            unfocusedBorderColor = Color(0xFFEEEEEE),
                            focusedLabelColor = Color(0xFFFF6B35),
                            cursorColor = Color(0xFFFF6B35)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Column(Modifier.fillMaxWidth()) {
                    LabelText("Bio")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        placeholder = { Text("Ceritakan sedikit hobi Anda...", color = Color.LightGray) },
                        leadingIcon = { Icon(Icons.Default.Description, null, tint = Color(0xFFFF6B35)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF6B35),
                            unfocusedBorderColor = Color(0xFFEEEEEE),
                            focusedLabelColor = Color(0xFFFF6B35),
                            cursorColor = Color(0xFFFF6B35)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Column(Modifier.fillMaxWidth()) {
                    LabelText("Nomor HP")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = noHp,
                        onValueChange = { noHp = it },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color(0xFFFF6B35)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF6B35),
                            unfocusedBorderColor = Color(0xFFEEEEEE),
                            focusedLabelColor = Color(0xFFFF6B35),
                            cursorColor = Color(0xFFFF6B35)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = { viewModel.updateProfile(username, bio, noHp, selectedImageUri, context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFFFF6B35)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(listOf(Color(0xFFFF6B35), Color(0xFFFF8E5E)))),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Simpan Perubahan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}