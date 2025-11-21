package com.example.profilecardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profilecardapp.ui.theme.ProfileCardAppTheme
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: ProfileViewModel = hiltViewModel()

            ProfileCardAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {
                        HomeScreen(
                            onGoProfile = { navController.navigate("profile") },
                            onGoFeeds = { navController.navigate("feeds") }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            viewModel = vm,
                            onEdit = { navController.navigate("edit") }
                        )
                    }

                    composable("edit") {
                        EditProfileScreen(
                            viewModel = vm,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("feeds") {
                        FeedsScreen(
                            viewModel = vm,
                            onBack = { navController.popBackStack() }
                        )
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