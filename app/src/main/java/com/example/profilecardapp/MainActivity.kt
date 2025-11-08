package com.example.profilecardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profilecardapp.ui.theme.ProfileCardAppTheme
import com.example.profilecardapp.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardAppTheme {
                val navController = rememberNavController()
                val vm: ProfileViewModel = viewModel()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(onGoProfile = { navController.navigate("profile") }) }
                    composable("profile") {
                        ProfileScreen(
                            viewModel = vm,
                            onEdit = { navController.navigate("edit") }
                        )
                    }
                    composable("edit") {
                        EditProfileScreen(viewModel = vm, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

fun openExternalLink(context: android.content.Context, uri: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
