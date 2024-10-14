package com.example.minigames.gamestate.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.minigames.R
import com.example.minigames.games.FlappyBird.Bird
import com.example.minigames.games.FlappyBird.FlappyBirdGameLogic
import com.example.minigames.games.FlappyBird.Pipe
import com.example.minigames.gamestate.GameState

class FlappyBirdGameState(private var canvasWidth: Float = 1440f, private var canvasHeight: Float = 1526f) : GameState() {

    // Mutable state to trigger recompositions when these values change
    var isGameOver by mutableStateOf(false)
        private set

    // Reference to the game logic
    var gameLogic = FlappyBirdGameLogic(canvasHeight = canvasHeight, canvasWidth = canvasWidth)

    // Wrap bird and pipes in MutableState to trigger recompositions when they change
    val bird: Bird get() = gameLogic.bird
    var pipes = mutableStateListOf<Pipe>()
    var currentScore by mutableStateOf(0)
    var deaths by mutableStateOf(0)
    var isGameEnded by mutableStateOf(false)
    var currentHighScore by mutableStateOf(0) // Track high score


    // Initialize the game with the game logic and canvas dimensions
    fun initializeGame() {
        gameLogic.startNewGame()
        gameStartTime = System.currentTimeMillis()

        // Set initial values for game state
        bird.x = canvasWidth * 0.1f
        bird.y = canvasHeight / 2
        pipes.clear()
        pipes.addAll(gameLogic.pipes)
        currentScore = 0
        deaths = 0
        isGameOver = false
    }

    override fun getThemeMusic(): Int {
        return R.raw.flappy_bird
    }

    fun pauseGame() {
        currentState = State.PAUSED
        isTPaused = true
        pauseTime = System.currentTimeMillis()
    }

    fun resumeGame() {
        currentState = State.RUNNING
        isTPaused = false
        gameStartTime += (System.currentTimeMillis() - pauseTime) // Adjust for paused time
    }

    fun getPipeWidth(): Float {
        return gameLogic.pipeWidth
    }

    fun birdFlap() {
        gameLogic.birdFlap()
    }

    fun updateGame() {
        if (currentState == State.PAUSED) return

        gameLogic.updateGame()
        updateElapsedTime()

        currentScore = gameLogic.score
        deaths = gameLogic.deathCount
        isGameEnded = gameLogic.isGameOver

        pipes.clear()
        pipes.addAll(gameLogic.pipes)

        // Update high score if the current score is higher
        if (currentScore > currentHighScore) {
            currentHighScore = currentScore
        }

        if (isGameEnded) {
            isGameOver = true
            resetFlappyBirdGame()
        }
    }

    // Reset the game
    fun resetFlappyBirdGame() {
        gameLogic.resetGame()
        currentScore = 0
        deaths = 0
        isGameOver = false
    }

    fun setCanvasDimensions(canvasWidth: Float, canvasHeight: Float) {
        this.canvasWidth = canvasWidth
        this.canvasHeight = canvasHeight
        gameLogic.setCanvas(canvasWidth = canvasWidth, canvasHeight = canvasHeight)
    }
}
