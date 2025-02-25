package com.example.marktheland.presentation

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.marktheland.viewmodel.SessionViewModel

@Composable
fun SessionHistory(viewModel: SessionViewModel = hiltViewModel(), navController: NavHostController) {
    val sessions by viewModel.sessions.collectAsState() // 🔥 Now observing StateFlow

    LaunchedEffect(sessions) {
        Log.d("SessionHistory", "Updated sessions list: ${sessions.size}")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Session History", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if (sessions.isEmpty()) {
            Text(
                text = "No sessions available",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyColumn {
                items(sessions) { session ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        session.imagePath?.let { imagePath ->
                            val bitmap = BitmapFactory.decodeFile(imagePath)
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Session Image",
                                    modifier = Modifier.size(80.dp)
                                )
                            } ?: Text("Image not found", color = Color.Red)
                        }

                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(text = session.name, fontWeight = FontWeight.Bold)
                            Text(text = session.timestamp, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
