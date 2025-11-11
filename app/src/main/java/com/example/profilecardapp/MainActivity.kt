package com.example.profilecardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profilecardapp.data.ProfileDatabase
import com.example.profilecardapp.data.ProfileRepository
import com.example.profilecardapp.ui.theme.ProfileCardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = ProfileDatabase.getDatabase(context = this)
            val userDao = db.userDao()
            val followerDao = db.followerDao()
            val repository = ProfileRepository(userDao, followerDao)

            val vm: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(repository)
            )

            ProfileCardAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(onGoProfile = { navController.navigate("profile") })
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
