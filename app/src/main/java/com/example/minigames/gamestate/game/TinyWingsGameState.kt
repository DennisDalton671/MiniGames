package com.example.minigames.gamestate.game

import com.example.minigames.R
import com.example.minigames.gamestate.GameState

class TinyWingsGameState() : GameState() {



    // Override to return Snake game's theme music
    override fun getThemeMusic(): Int {
        return R.raw.tiny_wings // Snake game's music resource
    }
}