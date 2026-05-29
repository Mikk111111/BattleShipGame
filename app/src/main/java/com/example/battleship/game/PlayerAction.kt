package com.example.battleship.game

class PlayerAction(
    private val data: Data
) {
    fun shoot(x: Int, y: Int): Boolean {
        if (!isInArray(x, y)) return false
        if (isPlayerShotDuplicate(x, y)) return false

        return if (isPlayerShotConnected(x, y)) {
            data.playerShotData[x][y] = "x"
            isTheShipDestroyed(data.enemyShipData[x][y], x, y)
            true
        } else {
            data.playerShotData[x][y] = "*"
            true
        }
    }

    private fun isTheShipDestroyed(shipData: String, x: Int, y: Int) {
        val length = shipData.toIntOrNull() ?: return
        var shipHealth = length - 1

        var checkUp = true
        var checkRight = true
        var checkDown = true
        var checkLeft = true

        for (i in 1..length) {
            if (checkUp) {
                if (isInArray(x + i, y) && data.playerShotData[x + i][y] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        beginDestructionSequenceColumn(x, y, length)
                        return
                    }
                } else {
                    checkUp = false
                }
            }

            if (checkRight) {
                if (isInArray(x, y + i) && data.playerShotData[x][y + i] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        beginDestructionSequenceLine(x, y, length)
                        return
                    }
                } else {
                    checkRight = false
                }
            }

            if (checkDown) {
                if (isInArray(x - i, y) && data.playerShotData[x - i][y] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        beginDestructionSequenceColumn(x, y, length)
                        return
                    }
                } else {
                    checkDown = false
                }
            }

            if (checkLeft) {
                if (isInArray(x, y - i) && data.playerShotData[x][y - i] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        beginDestructionSequenceLine(x, y, length)
                        return
                    }
                } else {
                    checkLeft = false
                }
            }
        }
    }

    private fun beginDestructionSequenceLine(x: Int, y: Int, length: Int) {
        println("Destroyed enemy ship length=$length at x=$x y=$y")
        data.totalEnemyShipHealth -= length

        var startY = y

        for (i in 0 until length) {
            if (isInArray(x, y - i) && data.playerShotData[x][y - i] == "x") {
                startY = y - i
            }
        }

        for (u in 0 until length) {
            for (i in -1..1) {
                for (j in -1..1) {
                    val targetX = x + i
                    val targetY = startY + j + u

                    if (isInArray(targetX, targetY)) {
                        if (
                            data.playerShotData[targetX][targetY] == "0" &&
                            data.playerShotData[x][startY + u] == "x"
                        ) {
                            data.playerShotData[targetX][targetY] = "*"
                        }
                    }
                }
            }
        }
    }

    private fun beginDestructionSequenceColumn(x: Int, y: Int, length: Int) {
        println("Destroyed enemy COLUMN ship length=$length at x=$x y=$y")

        data.totalEnemyShipHealth -= length

        var startX = x

        // find topmost connected X
        while (isInArray(startX - 1, y) && data.playerShotData[startX - 1][y] == "x") {
            startX--
        }

        for (u in 0 until length) {
            val shipX = startX + u

            if (!isInArray(shipX, y)) continue
            if (data.playerShotData[shipX][y] != "x") continue

            for (dx in -1..1) {
                for (dy in -1..1) {
                    val targetX = shipX + dx
                    val targetY = y + dy

                    if (
                        isInArray(targetX, targetY) &&
                        data.playerShotData[targetX][targetY] == "0"
                    ) {
                        data.playerShotData[targetX][targetY] = "*"
                    }
                }
            }
        }
    }

    private fun isPlayerShotDuplicate(x: Int, y: Int): Boolean {
        return data.playerShotData[x][y] != "0"
    }

    private fun isPlayerShotConnected(x: Int, y: Int): Boolean {
        return data.enemyShipData[x][y] != "0"
    }

    private fun isInArray(x: Int, y: Int): Boolean {
        return x in 0 until data.getArraySize() &&
                y in 0 until data.getArraySize()
    }
}