package com.example.battleship.game

import kotlin.random.Random

class AIShips(
    private val data: Data
) {
    private var checkResult = true

    fun shipAutoPrint(length: Int) {
        do {
            val x = Random.nextInt(data.getArraySize())
            val y = Random.nextInt(data.getArraySize())
            randomEntry(length, x, y)
        } while (!checkResult)

        data.totalEnemyShipHealth += length
    }

    private fun randomEntry(length: Int, x: Int, y: Int) {
        checkResult = true

        val randomDirection = Random.nextInt(1, 5)

        val positions = when (randomDirection) {
            1 -> List(length) { x + it to y }
            2 -> List(length) { x to y + it }
            3 -> List(length) { x - it to y }
            else -> List(length) { x to y - it }
        }

        if (positions.all { (px, py) -> isNearShips(px, py) }) {
            positions.forEach { (px, py) ->
                data.enemyShipData[px][py] = length.toString()
            }
        } else {
            checkResult = false
        }
    }

    private fun isNearShips(x: Int, y: Int): Boolean {
        if (x !in 0 until data.getArraySize()) return false
        if (y !in 0 until data.getArraySize()) return false

        if (data.enemyShipData[x][y] != "0") return false

        for (i in -1..1) {
            for (j in -1..1) {
                val nx = x + i
                val ny = y + j

                if (nx in 0 until data.getArraySize() &&
                    ny in 0 until data.getArraySize()
                ) {
                    if (data.enemyShipData[nx][ny] != "0") {
                        return false
                    }
                }
            }
        }

        return true
    }
}