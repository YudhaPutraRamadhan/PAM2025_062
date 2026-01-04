package com.example.hobbyyk_new.view.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hobbyyk_new.data.model.Activity
import com.example.hobbyyk_new.utils.Constants
import com.example.hobbyyk_new.viewmodel.ActivityViewModel
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityListScreen(navController: NavController, communityId: Int) {
    val viewModel: ActivityViewModel = viewModel()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var activityToDeleteId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(communityId) {
        viewModel.getActivities(communityId)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(28.dp),
            title = { Text("Hapus Aktivitas?", fontWeight = FontWeight.Black) },
            text = { Text("Tindakan ini permanen. Jadwal dan foto kegiatan akan dihapus dari sistem.") },
            confirmButton = {
                Button(
                    onClick = {
                        activityToDeleteId?.let { id -> viewModel.deleteActivity(id, communityId) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Ya, Hapus", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            TopAppBar(
                title = { Text("Kelola Aktivitas", fontWeight = FontWeight.Black, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("activity_form/$communityId/0") },
                containerColor = Color(0xFFFF6B35),
                shape = RoundedCornerShape(18.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, "Tambah", tint = Color.White, modifier = Modifier.size(28.dp))
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFFF6B35))
            } else if (viewModel.activityList.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Event, null, modifier = Modifier.size(64.dp), tint = Color(0xFFE0E0E0))
                    Spacer(Modifier.height(12.dp))
                    Text("Belum ada jadwal kegiatan.", color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(viewModel.activityList) { activity ->
                        ActivityItemCard(
                            activity = activity,
                            onDelete = {
                                activityToDeleteId = activity.id
                                showDeleteDialog = true
                            },
                            onEdit = { navController.navigate("activity_form/$communityId/${activity.id}") },
                            onDetail = { navController.navigate("detail_activity/${activity.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItemCard(activity: Activity, onDelete: () -> Unit, onEdit: () -> Unit, onDetail: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val firstImage = remember(activity.foto_kegiatan) {
                    try {
                        val jsonArray = JSONArray(activity.foto_kegiatan)
                        if (jsonArray.length() > 0) jsonArray.getString(0) else null
                    } catch (e: Exception) { null }
                }

                if (firstImage != null) {
                    AsyncImage(
                        model = "${Constants.URL_GAMBAR_BASE}$firstImage",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF5F5F5))
                    )
                } else {
                    Box(
                        Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFF6B35).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Event, null, tint = Color(0xFFFF6B35), modifier = Modifier.size(24.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        activity.judul_kegiatan,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color(0xFF1A1A1A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = activity.tanggal,
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B35),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Text(
                        activity.lokasi,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.background(Color(0xFFFFEBEE), CircleShape).size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, null, tint = Color(0xFFE53935), modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEEEEEE))
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp), tint = Color(0xFF424242))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
                }

                Button(
                    onClick = onDetail,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4361EE))
                ) {
                    Icon(Icons.Default.Visibility, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pratinjau", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}