package com.example.hobbyyk_new.view.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hobbyyk_new.R
import com.example.hobbyyk_new.viewmodel.LandingAppViewModel
import kotlinx.coroutines.delay

@Composable
fun LandingApp(
    navController: NavController
) {
    val viewModel: LandingAppViewModel = viewModel()

    LaunchedEffect(viewModel.startDestination) {
        delay(2000)
        viewModel.startDestination?.let { destination ->
            navController.navigate(destination) {
                popUpTo("auth_graph") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF6B35),
                        Color(0xFFF7931E),
                        Color(0xFFFFC107)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-50).dp, y = 100.dp)
                .size(200.dp)
                .alpha(0.1f)
                .clip(CircleShape)
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-20).dp)
                .size(150.dp)
                .alpha(0.1f)
                .clip(CircleShape)
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 100.dp)
                .size(180.dp)
                .alpha(0.08f)
                .clip(CircleShape)
                .background(Color.White)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.95f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFF3E0),
                                    Color(0xFFFFE0B2)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo HobbyYK",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "HobbyYK",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(50.dp)
            ) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(50.dp),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
                .size(120.dp)
                .alpha(0.1f)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
        )
    }
}