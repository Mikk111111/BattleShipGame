package com.example.battleship.data

data class ScoreEntry(
    val name: String,
    val score: Int,
    val turns: Int,
    val difficulty: String,
    val timestamp: Long
)