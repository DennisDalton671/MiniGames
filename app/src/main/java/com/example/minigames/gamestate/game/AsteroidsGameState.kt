package com.example.minigames.gamestate.game

import android.util.Log
import com.example.minigames.R
import com.example.minigames.games.Asteroids.Asteroid
import com.example.minigames.games.Asteroids.AsteroidsGameLogic
import com.example.minigames.games.Asteroids.Bullet
import com.example.minigames.games.Asteroids.Spaceship
import com.example.minigames.gamestate.GameState
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class AsteroidsGameState(private var canvasWidth: Float = 1440f, private var canvasHeight: Float = 1526f) : GameState() {

    // Create an instance of the game logic with canvas dimensions
    private val asteroidsGameLogic = AsteroidsGameLogic(canvasWidth, canvasHeight)

    // Define properties to track score, lives, and game over state
    var score: Int = 0
        private set
    var lives: Int = 3
        private set
    var isAGameOver: Boolean = false
        private set
    var isVisible: Boolean = true

    // Reference to game objects (spaceship, asteroids, bullets)
    val spaceship: Spaceship get() = asteroidsGameLogic.spaceship
    val asteroids: List<Asteroid> get() = asteroidsGameLogic.asteroids
    val bullets: List<Bullet> get() = asteroidsGameLogic.bullets


    // Game reset logic
    fun resetGameA() {
        isTPaused = true
        pauseTime = System.currentTimeMillis()
    }

    // Game start logic
    fun startNewGame() {
        asteroidsGameLogic.resetGame()
        score = asteroidsGameLogic.score
        lives = asteroidsGameLogic.lives
        isAGameOver = asteroidsGameLogic.isGameOver
        isVisible = asteroidsGameLogic.spaceship.isVisible
        isTPaused = false
        gameStartTime = System.currentTimeMillis()
    }

    // Game update logic
    fun updateGame() {
        asteroidsGameLogic.updateGame()

        // Update the score from the game logic
        score = asteroidsGameLogic.score

        lives = asteroidsGameLogic.lives

        isAGameOver = asteroidsGameLogic.isGameOver

        isVisible = asteroidsGameLogic.spaceship.isVisible
    }

    // Spaceship movement and speed control
    fun updateSpaceshipDirectionAndSpeed(angle: Float, speedMultiplier: Float) {
        Log.d("Spaceship Update", "Angle: $angle, Speed: $speedMultiplier")
        asteroidsGameLogic.updateSpaceshipMovement(angle = angle, speedMultiplier = speedMultiplier)
    }

    fun setCanvasDimensions(canvasWidth: Float, canvasHeight: Float) {
        this.canvasWidth = canvasWidth
        this.canvasHeight = canvasHeight
        asteroidsGameLogic.setCanvas(canvasWidth = canvasWidth, canvasHeight = canvasHeight)
    }


    // Override to return Asteroids game's theme music
    override fun getThemeMusic(): Int {
        return R.raw.blasting_shadows // Asteroids game's music resource
    }



}