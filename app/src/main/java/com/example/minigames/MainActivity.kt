package com.example.minigames

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.navigation.AppNavGraph
import com.example.minigames.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get SharedPreferences
        val preferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        setContent {

            // Initialize the ViewModel with a Factory
            val appViewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(
                    context = applicationContext,
                    preferences = preferences
                )
            )

            // Use AppTheme based on the dark mode state in the ViewModel
            AppTheme(isDarkTheme = appViewModel.isDarkTheme) {
                Surface(
                    color = AppTheme.colors.background // Use the current theme's background
                ) {
                    val navController = rememberNavController()

                    // Pass the ViewModel to the NavGraph
                    AppNavGraph(
                        navController = navController,
                        appViewModel = appViewModel // Pass the ViewModel to the NavGraph
                    )
                }
            }
        }
    }
}



class AppViewModelFactory(
    private val context: Context,
    private val preferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(context, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}