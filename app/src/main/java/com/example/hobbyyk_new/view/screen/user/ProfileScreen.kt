package com.example.hobbyyk_new.view.screen.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hobbyyk_new.data.datastore.UserStore
import com.example.hobbyyk_new.utils.Constants
import com.example.hobbyyk_new.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userStore = UserStore(context)
    val lifecycleOwner = LocalLifecycleOwner.current

    var showEmailDialog by remember { mutableStateOf(false) }
    var showPasswordConfirmDialog by remember { mutableStateOf(false) } // State baru untuk Dialog Password
    var tempNewEmail by remember { mutableStateOf("") }

    // --- LOGIKA NAVIGASI OTOMATIS ---
    LaunchedEffect(viewModel.navigateToVerifyPass) {
        if (viewModel.navigateToVerifyPass) {
            navController.navigate("verify_change_pass")
            viewModel.navigateToVerifyPass = false // Reset state agar tidak loop
        }
    }

    LaunchedEffect(viewModel.navigateToVerifyEmail) {
        viewModel.navigateToVerifyEmail?.let { email ->
            navController.navigate("verify_change_email/$email")
            viewModel.navigateToVerifyEmail = null // Reset state
        }
    }

    // Handle Toast Message
    LaunchedEffect(viewModel.message) {
        viewModel.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) { viewModel.fetchProfile() }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            TopAppBar(
                title = { Text("Profil Saya", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF1A1A1A)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF1A1A1A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (Bagian Foto Profil & Username tetap sama)
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFFF6B35), Color(0xFFFFB38E))))
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("${Constants.URL_GAMBAR_BASE}${viewModel.userProfile?.profile_pic}")
                                .crossfade(true).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    viewModel.userProfile?.username ?: "User",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    viewModel.userProfile?.email ?: "-",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                if (viewModel.managedCommunity != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFFFF6B35)),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFF6B35)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(44.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.VerifiedUser, null, tint = Color.White, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Admin Komunitas", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                Text(viewModel.managedCommunity?.nama_komunitas ?: "", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                InfoCard(
                    bio = viewModel.userProfile?.bio ?: "Belum ada bio.",
                    phone = viewModel.userProfile?.no_hp ?: "-",
                    onEditClick = { navController.navigate("edit_profile") }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Pengaturan Keamanan",
                    modifier = Modifier.align(Alignment.Start).padding(start = 4.dp),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A1A),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // GANTI PASSWORD SEKARANG MEMBUKA DIALOG KONFIRMASI
                SecurityButton(
                    title = "Ganti Password",
                    icon = Icons.Default.Lock,
                    onClick = { showPasswordConfirmDialog = true }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SecurityButton(
                    title = "Ganti Email",
                    icon = Icons.Default.Email,
                    onClick = { showEmailDialog = true }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextButton(
                    onClick = {
                        scope.launch {
                            userStore.clearSession()
                            navController.navigate("login") { popUpTo(0) { inclusive = true } }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE53935))
                ) {
                    Text("Keluar dari Akun", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }

        if (showEmailDialog) {
            AlertDialog(
                onDismissRequest = { showEmailDialog = false },
                shape = RoundedCornerShape(28.dp),
                title = { Text("Ganti Email", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Masukkan email baru Anda. Kami akan mengirimkan kode OTP untuk verifikasi.", fontSize = 14.sp, color = Color.Gray)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = tempNewEmail,
                            onValueChange = { tempNewEmail = it },
                            label = { Text("Email Baru") },
                            placeholder = { Text("contoh@email.com") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFF6B35),
                                unfocusedBorderColor = Color(0xFFCCCCCC),
                                focusedLabelColor = Color(0xFFFF6B35),
                                cursorColor = Color(0xFFFF6B35)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { if (tempNewEmail.isNotEmpty()) { viewModel.requestOtpEmail(tempNewEmail); showEmailDialog = false } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35)),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !viewModel.isLoading
                    ) {
                        if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        else Text("Kirim OTP")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEmailDialog = false }) { Text("Batal", color = Color.Gray) }
                }
            )
        }

        if (showPasswordConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showPasswordConfirmDialog = false },
                shape = RoundedCornerShape(28.dp),
                icon = { Icon(Icons.Default.Lock, null, tint = Color(0xFFFF6B35)) },
                title = { Text("Ubah Password?", fontWeight = FontWeight.Bold) },
                text = { Text("Sistem akan mengirimkan kode OTP ke email Anda untuk melanjutkan proses perubahan password.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.requestOtpPassword()
                            showPasswordConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Ya, Kirim OTP") }
                },
                dismissButton = {
                    TextButton(onClick = { showPasswordConfirmDialog = false }) { Text("Batal", color = Color.Gray) }
                }
            )
        }
    }
}

@Composable
fun InfoCard(bio: String, phone: String, onEditClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("Tentang Saya", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.background(Color(0xFFFFF3E0), CircleShape).size(36.dp)
                ) {
                    Icon(Icons.Default.Edit, "Edit", tint = Color(0xFFFF6B35), modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = bio, fontSize = 14.sp, color = Color(0xFF616161), lineHeight = 20.sp)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF5F5F5))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Phone, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = phone, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A))
            }
        }
    }
}

@Composable
fun SecurityButton(title: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(36.dp).background(Color(0xFFF5F5F5), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color(0xFF424242))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF424242), modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color(0xFFBDBDBD), modifier = Modifier.size(20.dp))
        }
    }
}