package com.example.hobbyyk_new.view.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hobbyyk_new.data.model.Activity
import com.example.hobbyyk_new.utils.Constants
import com.example.hobbyyk_new.viewmodel.ActivityViewModel
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFeedScreen(navController: NavController) {
    val viewModel: ActivityViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.fetchActivityFeed()
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            TopAppBar(
                title = { Text("Aktivitas Terbaru", fontWeight = FontWeight.Black, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFFF6B35))
            } else if (viewModel.feedList.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Belum ada aktivitas baru.", color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(viewModel.feedList) { activity ->
                        FeedItemCard(
                            activity = activity,
                            onClick = { navController.navigate("detail_activity/${activity.id}") },
                            onCommunityClick = { communityId ->
                                navController.navigate("community_detail/$communityId")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedItemCard(
    activity: Activity,
    onClick: () -> Unit,
    onCommunityClick: (Int) -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header: Info Komunitas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activity.community?.id?.let { onCommunityClick(it) } },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("${Constants.URL_GAMBAR_BASE}${activity.community?.foto_url}")
                            .crossfade(true).build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF5F5F5))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = activity.community?.nama_komunitas ?: "HobbyYK Community",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1A1A1A)
                        )
                        Text(
                            text = activity.tanggal,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Image Content
            val firstImage = remember(activity.foto_kegiatan) {
                try {
                    val jsonArray = JSONArray(activity.foto_kegiatan)
                    if (jsonArray.length() > 0) jsonArray.getString(0) else null
                } catch (e: Exception) { null }
            }

            if (firstImage != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.URL_GAMBAR_BASE}$firstImage")
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(18.dp))
                )
            }

            // Bottom Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = activity.judul_kegiatan,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = activity.lokasi,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}