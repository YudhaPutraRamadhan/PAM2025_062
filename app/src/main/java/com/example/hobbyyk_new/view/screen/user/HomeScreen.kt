package com.example.hobbyyk_new.view.screen.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hobbyyk_new.data.datastore.UserStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userStore = UserStore(context)

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "HobbyYK",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = Color(0xFFFF6B35),
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFFF6B35), Color(0xFFFF9431))
                                ),
                                shape = CircleShape
                            )
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil Saya",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Halo, Sobat Hobi!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1A1A1A),
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Eksplorasi keseruan komunitas di Yogyakarta hari ini.",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
                lineHeight = 20.sp
            )

            MenuCard(
                title = "Eksplor Komunitas",
                subtitle = "Temukan teman sehobi di Jogja",
                icon = Icons.Default.Groups,
                color = Color(0xFFFF6B35),
                onClick = { navController.navigate("community_list") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Aktivitas Terbaru",
                subtitle = "Lihat Aktivitas seru Komunitas mu",
                icon = Icons.Default.Event,
                color = Color(0xFF4361EE),
                onClick = { navController.navigate("activity_feed") }
            )
        }
    }
}

@Composable
fun MenuCard(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(color, color.copy(alpha = 0.8f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF757575),
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}