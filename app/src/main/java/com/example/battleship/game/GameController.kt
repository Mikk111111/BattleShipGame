package com.example.battleship.game

class GameController {

    val data = Data()

    private val enemyShips = AIShips(data)
    private val playerShips = Ships(data)
    private val playerAction = PlayerAction(data)
    private val ai = AIv2(data)

    enum class Difficulty {
        EASY,
        NORMAL,
        HARD
    }
    var currentDifficulty = Difficulty.NORMAL
        private set

    fun startNewGame(difficulty: Difficulty = Difficulty.NORMAL) {
        currentDifficulty = difficulty

        data.resetBoard()

        data.resetScore(
            when (difficulty) {
                Difficulty.EASY -> 1
                Difficulty.NORMAL -> 2
                Difficulty.HARD -> 3
            }
        )

        ai.hardMode = difficulty == Difficulty.HARD

        when (difficulty) {

            Difficulty.EASY -> {

                enemyShips.shipAutoPrint(4)
                enemyShips.shipAutoPrint(4)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(2)

                playerShips.shipAutoPrint(4)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(2)
                playerShips.shipAutoPrint(2)
            }

            Difficulty.NORMAL -> {

                enemyShips.shipAutoPrint(4)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(2)
                enemyShips.shipAutoPrint(2)

                playerShips.shipAutoPrint(4)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(2)
                playerShips.shipAutoPrint(2)
            }

            Difficulty.HARD -> {

                enemyShips.shipAutoPrint(4)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(3)
                enemyShips.shipAutoPrint(2)
                enemyShips.shipAutoPrint(2)

                playerShips.shipAutoPrint(4)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(3)
                playerShips.shipAutoPrint(2)
                playerShips.shipAutoPrint(2)
            }
        }
        data.maxPlayerShipHealth = data.totalPlayerShipHealth
        data.maxEnemyShipHealth = data.totalEnemyShipHealth
    }

    fun playerShoot(x: Int, y: Int): Boolean {
        if (data.isGameOver()) return false

        // prevent duplicate shots
        if (data.playerShotData[x][y] != "0") {
            return false
        }

        val shotWorked = playerAction.shoot(x, y)
        if (!shotWorked) return false

        data.updateScore(data.playerShotData[x][y])
        data.addTurn()

        if (data.totalEnemyShipHealth <= 0) {
            data.setEndScore()
            return true
        }

        ai.shoot()
        return true
    }

    fun getWinner(): String? {
        return when {
            data.totalEnemyShipHealth == 0 -> "Player"
            data.totalPlayerShipHealth == 0 -> "AI"
            else -> null
        }
    }
}