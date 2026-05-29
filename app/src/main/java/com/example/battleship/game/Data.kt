package com.example.battleship.game

class Data {
    private val arraySize = 10

    val playerShipData = Array(arraySize) { Array(arraySize) { "0" } }
    val playerShotData = Array(arraySize) { Array(arraySize) { "0" } }
    val enemyShipData = Array(arraySize) { Array(arraySize) { "0" } }
    val enemyShotData = Array(arraySize) { Array(arraySize) { "0" } }

    var totalPlayerShipHealth = 0
    var totalEnemyShipHealth = 0
    var score = 1000
    var turns = 0
    var maxPlayerShipHealth = 0
    var maxEnemyShipHealth = 0

    fun resetBoard() {
        for (i in 0 until arraySize) {
            for (j in 0 until arraySize) {
                playerShipData[i][j] = "0"
                playerShotData[i][j] = "0"
                enemyShipData[i][j] = "0"
                enemyShotData[i][j] = "0"
            }
        }

        totalPlayerShipHealth = 0
        totalEnemyShipHealth = 0
        turns = 0
    }

    fun isGameOver(): Boolean {
        return totalEnemyShipHealth == 0 || totalPlayerShipHealth == 0
    }

    fun resetScore(diff: Int) {
        score = when (diff) {
            1 -> 500
            2 -> 1000
            3 -> 1500
            else -> 1000
        }
    }

    fun updateScore(symbol: String) {
        if (symbol == "x") {
            score += 50
        } else if (symbol == "*") {
            score -= 10
        }
    }

    fun setEndScore() {
        score -= turns * 10
    }

    fun addTurn() {
        turns++
    }

    fun getArraySize(): Int = arraySize
}