package com.example.hobbyyk_new.view.screen.admin

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
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
import com.example.hobbyyk_new.utils.uriToFile
import com.example.hobbyyk_new.view.screen.auth.LabelText
import com.example.hobbyyk_new.viewmodel.AdminCommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCommunityScreen(navController: NavController, communityId: Int) {
    val viewModel: AdminCommunityViewModel = viewModel()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(communityId) { viewModel.fetchCommunityById(communityId) }

    var nama by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kontak by remember { mutableStateOf("") }
    var linkGrup by remember { mutableStateOf("") }

    var newLogoUri by remember { mutableStateOf<Uri?>(null) }
    var newBannerUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.myCommunity) {
        viewModel.myCommunity?.let {
            if (nama.isEmpty()) nama = it.nama_komunitas
            if (kategori.isEmpty()) kategori = it.kategori
            if (deskripsi.isEmpty()) deskripsi = it.deskripsi ?: ""
            if (lokasi.isEmpty()) lokasi = it.lokasi ?: ""
            if (kontak.isEmpty()) kontak = it.kontak ?: ""
            if (linkGrup.isEmpty()) linkGrup = it.link_grup ?: ""
        }
    }

    val logoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { newLogoUri = it }
    val bannerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { newBannerUri = it }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil Komunitas", fontWeight = FontWeight.Black, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (viewModel.isLoading && nama.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF6B35))
            }
        } else {
            Column(
                modifier = Modifier.padding(paddingValues).fillMaxSize().padding(horizontal = 24.dp).verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text("Visual Community", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF1A1A1A))
                Text("Klik pada gambar untuk memperbarui visual.", fontSize = 13.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Logo Edit
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.size(110.dp).clickable { logoLauncher.launch("image/*") }
                        ) {
                            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)).background(Color(0xFFF5F5F5))) {
                                val model = newLogoUri ?: "${Constants.URL_GAMBAR_BASE}${viewModel.myCommunity?.foto_url}"
                                AsyncImage(model = ImageRequest.Builder(context).data(model).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            }
                            Surface(modifier = Modifier.size(32.dp).offset(x = 4.dp, y = 4.dp), shape = CircleShape, color = Color(0xFFFF6B35), shadowElevation = 4.dp) {
                                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.padding(6.dp))
                            }
                        }
                    }

                    // Banner Edit
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(2f)) {
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.height(110.dp).fillMaxWidth().clickable { bannerLauncher.launch("image/*") }
                        ) {
                            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)).background(Color(0xFFF5F5F5))) {
                                val bannerModel = newBannerUri ?: "${Constants.URL_GAMBAR_BASE}${viewModel.myCommunity?.banner_url ?: viewModel.myCommunity?.foto_url}"
                                AsyncImage(model = ImageRequest.Builder(context).data(bannerModel).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            }
                            Surface(modifier = Modifier.size(32.dp).offset(x = 4.dp, y = 4.dp), shape = CircleShape, color = Color(0xFFFF6B35), shadowElevation = 4.dp) {
                                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.padding(6.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                LabelText("Nama Komunitas")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = nama, onValueChange = { nama = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF6B35), unfocusedBorderColor = Color(0xFFEEEEEE)))

                Spacer(modifier = Modifier.height(20.dp))

                LabelText("Kategori")
                Spacer(Modifier.height(8.dp))
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = kategori, onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF6B35), unfocusedBorderColor = Color(0xFFEEEEEE))
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                        Constants.COMMUNITY_CATEGORIES.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { kategori = option; expanded = false })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                LabelText("Lokasi")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = lokasi, onValueChange = { lokasi = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF6B35), unfocusedBorderColor = Color(0xFFEEEEEE)))

                Spacer(modifier = Modifier.height(20.dp))

                LabelText("Deskripsi")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = deskripsi, onValueChange = { deskripsi = it }, modifier = Modifier.fillMaxWidth(), minLines = 4, shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF6B35), unfocusedBorderColor = Color(0xFFEEEEEE)))

                Spacer(modifier = Modifier.height(20.dp))

                LabelText("Kontak Admin")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = kontak, onValueChange = { kontak = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF6B35), unfocusedBorderColor = Color(0xFFEEEEEE)))

                Spacer(modifier = Modifier.height(20.dp))

                LabelText("Link Grup WhatsApp")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = linkGrup, onValueChange = { linkGrup = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF6B35), unfocusedBorderColor = Color(0xFFEEEEEE)))

                Spacer(modifier = Modifier.height(56.dp))

                Button(
                    onClick = {
                        val logoFile = if (newLogoUri != null) uriToFile(newLogoUri!!, context) else null
                        val bannerFile = if (newBannerUri != null) uriToFile(newBannerUri!!, context) else null
                        viewModel.updateCommunity(communityId, nama, lokasi, deskripsi, kategori, kontak, linkGrup, logoFile, bannerFile)
                        Toast.makeText(context, "Pembaruan Berhasil!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFFFF6B35)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35))
                ) {
                    if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    else Text("Simpan Perubahan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}