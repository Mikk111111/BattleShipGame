package com.example.battleship.data

import android.content.Context
import androidx.core.content.edit

class SettingsStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun savePlayerName(name: String) {
        prefs.edit() {
            putString("player_name", name)
        }
    }

    fun getPlayerName(): String {
        return prefs.getString("player_name", "Player") ?: "Player"
    }
}