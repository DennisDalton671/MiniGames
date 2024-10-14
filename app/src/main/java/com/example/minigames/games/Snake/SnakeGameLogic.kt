package com.example.minigames.games.Snake

class SnakeGameLogic(val gridWidth: Int, val gridHeight: Int) {
    val board: Array<Array<TileType>> = Array(gridWidth) { Array(gridHeight) { TileType.EMPTY } }
    var inputLocked = false

    // Initialize the snake with length 3, starting at position (5,5)
    var snake = mutableListOf(
        Position(5, 5),
        Position(5, 4),
        Position(5, 3)
    )
    var direction = Direction.RIGHT // Initial direction
    var food = generateFood()

    var gameOver = false


    // Enum for different types of tiles on the grid
    enum class TileType {
        EMPTY, SNAKE, FOOD
    }

    // Possible directions
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    // Generate food in an unoccupied tile
    fun generateFood(): Position {
        val availableTiles = mutableListOf<Position>()
        for (x in 0 until gridWidth) {
            for (y in 0 until gridHeight) {
                if (board[x][y] == TileType.EMPTY) {
                    availableTiles.add(Position(x, y))
                }
            }
        }
        return availableTiles.random()
    }

    // Move the snake and update the board
    fun moveSnake() {
        if (gameOver) return

        val head = snake.first()
        val newHead = when (direction) {
            Direction.LEFT -> Position(head.x, (head.y - 1)) // y decreases as you move up
            Direction.RIGHT -> Position(head.x, (head.y + 1)) // y increases as you move down
            Direction.UP -> Position((head.x - 1), head.y) // x decreases as you move left
            Direction.DOWN -> Position((head.x + 1), head.y) // x increases as you move right
        }

        // Check for self-collision or wall collision
        if (newHead.x < 0 || newHead.x >= gridWidth || newHead.y < 0 || newHead.y >= gridHeight || board[newHead.x][newHead.y] == TileType.SNAKE) {
            gameOver = true
            return
        }

        // Update snake body
        snake.add(0, newHead)
        board[newHead.x][newHead.y] = TileType.SNAKE // Mark new head position

        // Check if the snake eats food
        if (newHead == food) {
            food = generateFood()
            board[food.x][food.y] = TileType.FOOD // Mark new food position
        } else {
            // Remove the last segment if no food is eaten
            val tail = snake.removeLast()
            board[tail.x][tail.y] = TileType.EMPTY
        }
        // Unlock input after moving
        inputLocked = false
    }

    // Method to change the direction
    fun changeDirection(newDirection: Direction) {
        // Prevent the snake from reversing directly into itself and avoid multiple inputs
        if (inputLocked) return // Ignore input if locked

        // Prevent reversing directly into itself
        if ((direction == Direction.UP && newDirection == Direction.DOWN) ||
            (direction == Direction.DOWN && newDirection == Direction.UP) ||
            (direction == Direction.LEFT && newDirection == Direction.RIGHT) ||
            (direction == Direction.RIGHT && newDirection == Direction.LEFT)) {
            return // Ignore reverse direction
        }

        // Lock input after accepting one
        inputLocked = true
        direction = newDirection
    }

    // Function to reset the snake, food, and the board
    fun resetSnake() {
        // Reset the snake's position and length
        snake = mutableListOf(
            Position(5, 5),
            Position(5, 4),
            Position(5, 3)
        )
        direction = Direction.RIGHT // Reset direction

        // Reset the board and place the snake again
        for (x in 0 until gridWidth) {
            for (y in 0 until gridHeight) {
                board[x][y] = TileType.EMPTY
            }
        }
        // Place the snake on the board
        snake.forEach { position ->
            board[position.x][position.y] = TileType.SNAKE
        }

        // Regenerate the food and place it on the board
        food = generateFood()
        board[food.x][food.y] = TileType.FOOD

        gameOver = false // Reset game over state
    }

    // Initialize the game
    init {
        board[snake[0].x][snake[0].y] = TileType.SNAKE // Snake initial position
        board[food.x][food.y] = TileType.FOOD // Food initial position
    }
}