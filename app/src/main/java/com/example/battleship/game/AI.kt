package com.example.battleship.game

import kotlin.random.Random

class AI(
    private val data: Data
) {
    private var smartMode = false

    private var smartX = 0
    private var smartY = 0

    var hardMode = false

    fun shoot() {
        var validShot = false

        while (!validShot) {

            if (smartMode) {
                validShot = smartEnemyShot(smartX, smartY)
            } else {

                var x = Random.nextInt(10)
                var y = Random.nextInt(10)

                if (hardMode && Random.nextInt(10) == 5) {
                    loop@ for (i in 0 until data.getArraySize()) {
                        for (j in 0 until data.getArraySize()) {

                            if (
                                data.playerShipData[i][j] != "0" &&
                                data.enemyShotData[i][j] == "0"
                            ) {
                                x = i
                                y = j
                                break@loop
                            }
                        }
                    }
                }

                validShot = enemyTakingAim(x, y)
            }
        }
    }

    private fun enemyTakingAim(x: Int, y: Int): Boolean {

        if (data.enemyShotData[x][y] != "0") {
            return false
        }

        return if (data.playerShipData[x][y] != "0") {

            data.enemyShotData[x][y] = "x"

            smartMode = true
            smartX = x
            smartY = y

            isShipDestroyed(
                data.playerShipData[x][y],
                x,
                y
            )

            true

        } else {

            data.enemyShotData[x][y] = "*"
            true
        }
    }

    private fun smartEnemyShot(x: Int, y: Int): Boolean {

        val directions = listOf(
            Pair(-1, 0),
            Pair(0, 1),
            Pair(1, 0),
            Pair(0, -1)
        ).shuffled()

        for ((dx, dy) in directions) {

            val nx = x + dx
            val ny = y + dy

            if (!isInArray(nx, ny)) continue
            if (data.enemyShotData[nx][ny] != "0") continue

            if (data.playerShipData[nx][ny] != "0") {

                data.enemyShotData[nx][ny] = "x"

                smartX = nx
                smartY = ny

                isShipDestroyed(
                    data.playerShipData[nx][ny],
                    nx,
                    ny
                )

            } else {
                data.enemyShotData[nx][ny] = "*"
            }

            return true
        }

        smartMode = false
        return false
    }

    private fun isShipDestroyed(
        shipData: String,
        x: Int,
        y: Int
    ) {

        val length = shipData.toIntOrNull() ?: return
        var hits = 0

        for (i in 0 until data.getArraySize()) {
            for (j in 0 until data.getArraySize()) {

                if (
                    data.playerShipData[i][j] == shipData &&
                    data.enemyShotData[i][j] == "x"
                ) {
                    hits++
                }
            }
        }

        if (hits >= length) {

            smartMode = false

            destroyAroundShip(
                shipData,
                x,
                y
            )

            data.totalPlayerShipHealth -= length
        }
    }

    private fun destroyAroundShip(
        shipData: String,
        x: Int,
        y: Int
    ) {

        for (i in -1..1) {
            for (j in -1..1) {

                val nx = x + i
                val ny = y + j

                if (isInArray(nx, ny)) {

                    if (data.enemyShotData[nx][ny] == "0") {
                        data.enemyShotData[nx][ny] = "*"
                    }
                }
            }
        }
    }

    private fun isInArray(x: Int, y: Int): Boolean {

        return x in 0 until data.getArraySize() &&
                y in 0 until data.getArraySize()
    }
}