package com.example.minigames.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.theme.AppTheme

@Composable
fun Settings_UI(navController: NavController, appViewModel: AppViewModel) {
    // Get the current dark mode state from AppState
    val isDarkTheme = appViewModel.isDarkTheme
    // Get the current volume from AppState
    var volume by remember { mutableStateOf(appViewModel.volume) }

    AppTheme(isDarkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Dark Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark Mode",
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    color = AppTheme.colors.textColor
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = {
                        appViewModel.toggleTheme()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AppTheme.colors.buttonColor,
                        uncheckedThumbColor = AppTheme.colors.dPad
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Volume Control
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Music Volume", color = AppTheme.colors.textColor, fontSize = 18.sp)
                Slider(
                    value = volume,
                    onValueChange = {
                        volume = it
                        appViewModel.updateVolume(it)  // Use ViewModel to update volume
                    },
                    valueRange = 0f..1f,
                    modifier = Modifier.padding(16.dp)
                )
                Text(text = "Volume: ${(volume * 100).toInt()}%", color = AppTheme.colors.textColor)
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Back Button to navigate back to the Main Menu
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Back to Menu", color = AppTheme.colors.textColor)
            }
        }
    }
}