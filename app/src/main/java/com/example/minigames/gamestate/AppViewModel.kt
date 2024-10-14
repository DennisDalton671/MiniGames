package com.example.minigames.gamestate

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.minigames.R
import com.example.minigames.gamestate.game.AsteroidsGameState
import com.example.minigames.gamestate.game.FlappyBirdGameState
import com.example.minigames.gamestate.game.SnakeGameState
import com.example.minigames.gamestate.game.TinyWingsGameState

class AppViewModel(private val context: Context, private val preferences: SharedPreferences) : ViewModel() {
    // State to control whether the theme is dark mode
    var isDarkTheme by mutableStateOf(true)  // Default to light theme

    // GameState to store game-specific data (SnakeGameState, etc.)
    var gameState: GameState? by mutableStateOf(null)  // No game state until selected

    // Function to toggle between light and dark themes
    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }

    // Function to update volume
    fun updateVolume(newVolume: Float) {
        volume = newVolume
    }

    // Other GameState-related functions (if needed)
    fun startNewSnakeGame() {
        gameState = SnakeGameState(20,20)  // Reset game state
        startMusicForGame()
    }

    fun startNewAsteroidsGame() {
        gameState = AsteroidsGameState()
        startMusicForGame()
    }

    fun startNewFlappyBirdGame() {
        gameState = FlappyBirdGameState()
        startMusicForGame()
    }

    fun startNewTinyWingsGame() {
        gameState = TinyWingsGameState()
        startMusicForGame()
    }

    // MediaPlayer instance
    private var mediaPlayer: MediaPlayer? = null

    // Volume state (store in preferences, default is 50%)
    // Volume state (store in preferences, default is 50%)
    var volume: Float
        get() = preferences.getFloat("volume", 0.5f)  // Get volume from SharedPreferences
        set(value) {
            preferences.edit().putFloat("volume", value).apply()  // Save volume in SharedPreferences
            mediaPlayer?.setVolume(value, value)  // Adjust volume on the fly if MediaPlayer is playing
        }


    fun startMenuMusic() {
        stopMusic()

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.pixel_dreams) // Load your music file
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(volume, volume) // Set initial volume
            mediaPlayer?.start()
        }
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Ensure MediaPlayer resources are released when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        stopMusic()  // Stop music when ViewModel is destroyed
    }

    fun startMusicForGame() {
        stopMusic()  // Stop any currently playing music

        // Get the music resource from the current game state
        val musicResource = gameState?.getThemeMusic() ?: 0  // If null, default to 0

        // Check if the game state provides a valid music resource
        if (musicResource != 0) {
            // Create the MediaPlayer with the correct music for the game
            mediaPlayer = MediaPlayer.create(context, musicResource)

            if (mediaPlayer != null) {
                // Configure and start the music
                mediaPlayer?.isLooping = true
                mediaPlayer?.setVolume(volume, volume)
                mediaPlayer?.start()
            } else {
                // Log an error if MediaPlayer initialization fails
                Log.e("AppViewModel", "Failed to initialize MediaPlayer for resource: $musicResource")
            }
        } else {
            // Log an error if an invalid resource ID is provided
            Log.e("AppViewModel", "Invalid music resource: $musicResource")
        }
    }

}