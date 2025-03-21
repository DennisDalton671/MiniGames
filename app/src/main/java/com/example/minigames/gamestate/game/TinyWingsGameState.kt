package com.example.minigames.gamestate.game

import com.example.minigames.R
import com.example.minigames.games.TinyWings.Hill
import com.example.minigames.games.TinyWings.TinyWingsGameLogic
import com.example.minigames.gamestate.GameState

class TinyWingsGameState(
    private var canvasWidth: Float = 2400.0f,
    private var canvasHeight: Float = 888.0f
) : GameState() {

    private val gameLogic = TinyWingsGameLogic(canvasWidth, canvasHeight)  // Initialize game logic

    // Access the bird's position directly from game logic
    val birdPositionX: Float
        get() = gameLogic.birdPositionX

    val birdPositionY: Float
        get() = gameLogic.birdPositionY

    // Access hills directly from game logic
    val hills: List<Hill>
        get() = gameLogic.currentHills

    // Method to set or update the canvas size dynamically
    fun setCanvasSize(width: Float, height: Float) {
        canvasWidth = width
        canvasHeight = height
        gameLogic.setCanvasSize(width, height)  // Pass the dimensions to game logic
    }

    // Update method to advance the game state
    fun update(deltaTime: Float) {
        gameLogic.updateGame(deltaTime)  // Call the game logic's update method
    }

    // Function to trigger dive input
    fun performDive() {
        gameLogic.onDiveInput()  // Forward dive input to game logic
    }

    // Function to stop dive input
    fun stopDive() {
        gameLogic.onReleaseDive()  // Forward release input to game logic
    }

    // Override to return Tiny Wings theme music
    override fun getThemeMusic(): Int {
        return R.raw.tiny_wings  // Tiny Wings music resource
    }
}