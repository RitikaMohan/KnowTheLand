package com.example.marktheland.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marktheland.presentation.MainScreen
import com.example.marktheland.presentation.SessionHistory
import com.example.marktheland.viewmodel.SessionViewModel

@Composable
fun Navigation(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "mainScreen") {
        // 🔥 Main Screen Route
        composable("mainScreen") {
            MainScreen(sessionViewModel, navController)
        }

        // 🔥 Session History Route
        composable("sessionHistory") {
            SessionHistory(sessionViewModel, navController)
        }
    }
}


