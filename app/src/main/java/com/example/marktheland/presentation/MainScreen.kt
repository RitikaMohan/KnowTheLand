package com.example.marktheland.presentation

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.marktheland.data.TfliteLandmarkClassifier
import com.example.marktheland.domain.Classification
import com.example.marktheland.viewmodel.SessionViewModel


@Composable
fun MainScreen(sessionViewModel: SessionViewModel, navController: NavController) {
    var classifications by remember { mutableStateOf(emptyList<Classification>()) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val imageAnalyzer = remember {
        LandmarkImageAnalyzer(
            classifier = TfliteLandmarkClassifier(context),
            onResult = { classifications = it }
        )
    }
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(LifecycleCameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context), imageAnalyzer)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller, Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            classifications.forEach {
                Text(
                    text = it.name,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = {
                    capturedBitmap?.let { bitmap ->
                        val landmarkName = classifications.firstOrNull()?.name ?: "Unknown"
                        val timestamp = System.currentTimeMillis().toString()
                        sessionViewModel.saveSession(context, landmarkName, timestamp, bitmap)
                        Log.d("CaptureButton", "Saving session: $landmarkName at $timestamp")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Capture Landmark")
            }

            Button(
                onClick = { navController.navigate("sessionHistory") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("History")
            }
        }
    }
}
