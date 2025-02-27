package com.example.marktheland.presentation

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.marktheland.model.Session
import com.example.marktheland.viewmodel.SessionViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionHistory(viewModel: SessionViewModel = hiltViewModel(), navController: NavHostController) {
    val sessions by viewModel.sessions.collectAsState() // 🔥 Observe StateFlow

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Session History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFBCAAA4) // Match gradient color
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFBCAAA4), Color(0xFFD7CCC8)))) // Gradient BG
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (sessions.isEmpty()) {
                EmptyStateView()
            } else {
                LazyColumn {
                    items(sessions) { session ->
                        SessionCard(session) { clickedSession ->
                            // Navigate to details screen or perform action
                            navController.navigate("sessionDetails/${clickedSession.timestamp}")
                        }
                    }
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: String): String {
    return try {
        val inputFormat = SimpleDateFormat("hh:mm:ss dd-MM-yy", Locale.getDefault()) // Ensure this matches saved format
        val date = inputFormat.parse(timestamp)
        val outputFormat = SimpleDateFormat("hh:mm:ss dd-MM-yy", Locale.getDefault()) // Final format for display
        outputFormat.format(date!!)
    } catch (e: Exception) {
        timestamp // Fallback in case of error
    }
}


@Composable
fun SessionCard(session: Session, onClick: (Session) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            session.imagePath?.let { imagePath ->
                val bitmap = BitmapFactory.decodeFile(imagePath)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Session Image",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } ?: Text("Image not found", color = Color.Red)
            }

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = session.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = formatTimestamp(session.timestamp), fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = "No Sessions",
            modifier = Modifier.size(100.dp),
            tint = Color.LightGray
        )
        Text(
            text = "No sessions available",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
