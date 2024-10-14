package com.example.minigames.gamestate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.minigames.R

// Enum for game states


abstract class GameState {

    enum class State {
        RUNNING,
        PAUSED
    }


    protected var currentState: State = State.RUNNING

    var isTPaused: Boolean = false
    var points: Int = 0
    var retries: Int = 0 // Count how many times the user has retried a level
    var gameStartTime: Long = 0L
    var pauseTime: Long = 0L

    // Mutable state to trigger recompositions for time
    var elapsedTime by mutableStateOf(0L)


    open fun getThemeMusic(): Int {
        // Default music for general cases
        return R.raw.pixel_dreams
    }

    // Update elapsed time in every frame
    fun updateElapsedTime() {
        val currentTime = if (isTPaused) pauseTime else System.currentTimeMillis()
        elapsedTime = currentTime - gameStartTime
    }

    // Calculate elapsed time in MM:SS format
    fun getElapsedTimeFormattedLive(): String {
        if (gameStartTime == 0L) return "00:00"  // If the game hasn't started, return "00:00"

        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)  // Format as MM:SS
    }

    // Calculate elapsed time in MM:SS format
    fun getElapsedTimeFormatted(): String {
        if (gameStartTime == 0L) return "00:00"  // If the game hasn't started, return "00:00"

        val currentTime = if (isTPaused) pauseTime else System.currentTimeMillis()
        val elapsedMillis = currentTime - gameStartTime
        val seconds = (elapsedMillis / 1000) % 60
        val minutes = (elapsedMillis / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)  // Format as MM:SS
    }


    // Get the current state
    fun getState(): State {
        return currentState
    }

    // Reset the game to initial state
    fun resetGame() {
        currentState = State.RUNNING
        // Additional reset logic (e.g., reset score, bird position, etc.)
    }

    // Helper functions to check the game state
    fun isRunning(): Boolean {
        return currentState == State.RUNNING
    }

    fun isPaused(): Boolean {
        return currentState == State.PAUSED
    }



}