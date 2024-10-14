package com.example.minigames.gamestate.game

import com.example.minigames.R
import com.example.minigames.games.Snake.SnakeGameLogic
import com.example.minigames.gamestate.GameState
import com.example.minigames.ui.GameUI.Snake.BlockType


class SnakeGameState(val gridWidth: Int, val gridHeight: Int) : GameState() {
    private val gameLogic = SnakeGameLogic(gridWidth, gridHeight) // Core game mechanics

    // Board state tracking the snake and food positions
    val grid: Array<Array<BlockType>> = Array(gridWidth) { Array(gridHeight) { BlockType.EMPTY } }

    // Score and game over state
    var score: Int = 0
        private set
    var isSGameOver: Boolean = false
        private set
    var hasStarted = false


    init {
        updateGrid() // Initial grid setup
    }

    // Direction enum inside SnakeGameState, which maps to SnakeGameLogic's direction
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    fun resetGameS() {
        gameLogic.resetSnake()  // Reset snake position and food
        isSGameOver = false       // Reset game over state
        hasStarted = false       // Game not yet started
        isTPaused = true          // Pause the game until interaction

        // Do not reset retries, death count, or stats here
        updateGrid()
    }

    // Reset the stats explicitly when a new game starts
    fun startNewGame() {
        points = 0  // Reset points/score
        score = 0
        gameStartTime = System.currentTimeMillis()  // Reset timer
        isTPaused = false  // Unpause game
    }

    fun changeDirection(direction: Direction) {
        when (direction) {
            Direction.UP -> gameLogic.changeDirection(SnakeGameLogic.Direction.UP)
            Direction.DOWN -> gameLogic.changeDirection(SnakeGameLogic.Direction.DOWN)
            Direction.LEFT -> gameLogic.changeDirection(SnakeGameLogic.Direction.LEFT)
            Direction.RIGHT -> gameLogic.changeDirection(SnakeGameLogic.Direction.RIGHT)
        }
    }

    // Moves the snake and updates the grid
    fun moveSnake() {
        gameLogic.moveSnake() // Move the snake via game logic
        updateGrid() // Update the board/grid state
        isSGameOver = gameLogic.gameOver // Update game over status
        if(!hasStarted) {
            gameStartTime = System.currentTimeMillis()
            isTPaused = false
            hasStarted = true
        }
        if (isSGameOver) {
            retries++
            isTPaused = true
            pauseTime = System.currentTimeMillis()
            hasStarted = false
        }
        // Update the score if the snake grows (when the snake size increases)
        if (gameLogic.snake.size > score + 3) {
            score++
        }
    }

    // Update the grid based on the current snake and food positions
    private fun updateGrid() {
        // Clear the grid
        for (x in 0 until gridWidth) {
            for (y in 0 until gridHeight) {
                grid[x][y] = BlockType.EMPTY
            }
        }

        // Update grid with snake positions
        gameLogic.snake.forEach { position ->
            grid[position.x][position.y] = BlockType.SNAKE
        }

        // Update grid with food position
        grid[gameLogic.food.x][gameLogic.food.y] = BlockType.FOOD
    }

    // Override to return Snake game's theme music
    override fun getThemeMusic(): Int {
        return R.raw.snake_quest // Snake game's music resource
    }


}