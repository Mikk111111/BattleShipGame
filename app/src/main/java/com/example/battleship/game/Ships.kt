package com.example.battleship.game

import kotlin.random.Random

class Ships(
    private val data: Data
) {
    fun placePlayerShip(
        x: Int,
        y: Int,
        length: Int,
        direction: Int
    ): Boolean {
        val positions = getPositions(x, y, length, direction)

        if (positions.isEmpty()) return false
        if (!positions.all { (px, py) -> isNearShips(px, py) }) return false

        positions.forEach { (px, py) ->
            data.playerShipData[px][py] = length.toString()
        }

        data.totalPlayerShipHealth += length
        return true
    }

    fun shipAutoPrint(length: Int) {
        var placed = false

        while (!placed) {
            val x = Random.nextInt(data.getArraySize())
            val y = Random.nextInt(data.getArraySize())
            val direction = Random.nextInt(1, 5)

            placed = placePlayerShip(x, y, length, direction)
        }
    }

    private fun getPositions(
        x: Int,
        y: Int,
        length: Int,
        direction: Int
    ): List<Pair<Int, Int>> {
        return when (direction) {
            1 -> List(length) { x + it to y }
            2 -> List(length) { x to y + it }
            3 -> List(length) { x - it to y }
            4 -> List(length) { x to y - it }
            else -> emptyList()
        }
    }

    private fun isNearShips(x: Int, y: Int): Boolean {
        if (!isInsideBoard(x, y)) return false
        if (data.playerShipData[x][y] != "0") return false

        for (i in -1..1) {
            for (j in -1..1) {
                val nx = x + i
                val ny = y + j

                if (isInsideBoard(nx, ny)) {
                    if (data.playerShipData[nx][ny] != "0") {
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun isInsideBoard(x: Int, y: Int): Boolean {
        return x in 0 until data.getArraySize() &&
                y in 0 until data.getArraySize()
    }
}