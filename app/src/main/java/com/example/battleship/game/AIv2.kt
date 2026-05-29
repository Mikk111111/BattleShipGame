package com.example.battleship.game

import kotlin.random.Random

class AIv2(
    private val data: Data
) {
    var hardMode: Boolean = false

    private val targets = mutableListOf<Pair<Int, Int>>()
    private var firstHit: Pair<Int, Int>? = null
    private var direction: Pair<Int, Int>? = null

    fun shoot() {
        var shotDone = false

        while (!shotDone) {
            shotDone = when {
                direction != null -> shootAlongDirection()
                targets.isNotEmpty() -> shootTarget()
                else -> shootRandom()
            }
        }
    }

    private fun shootRandom(): Boolean {
        if (hardMode && Random.nextInt(20) == 5) {
            val cheatTarget = findKnownShipTarget()
            if (cheatTarget != null) {
                return shootCell(cheatTarget.first, cheatTarget.second)
            }
        }

        val availableCells = mutableListOf<Pair<Int, Int>>()

        for (x in 0 until data.getArraySize()) {
            for (y in 0 until data.getArraySize()) {
                if (data.enemyShotData[x][y] == "0") {
                    availableCells.add(x to y)
                }
            }
        }

        if (availableCells.isEmpty()) return false

        val target = availableCells.random()
        return shootCell(target.first, target.second)
    }

    private fun shootTarget(): Boolean {
        while (targets.isNotEmpty()) {
            val target = targets.removeAt(0)

            if (!isInArray(target.first, target.second)) continue
            if (data.enemyShotData[target.first][target.second] != "0") continue

            return shootCell(target.first, target.second)
        }

        firstHit = null
        direction = null
        return false
    }

    private fun shootAlongDirection(): Boolean {
        val start = firstHit ?: return false
        val dir = direction ?: return false

        val forward = findNextInDirection(start, dir)
        if (forward != null) {
            return shootCell(forward.first, forward.second)
        }

        val backwardDir = -dir.first to -dir.second
        val backward = findNextInDirection(start, backwardDir)
        if (backward != null) {
            return shootCell(backward.first, backward.second)
        }

        direction = null
        return shootTarget()
    }

    private fun findNextInDirection(
        start: Pair<Int, Int>,
        dir: Pair<Int, Int>
    ): Pair<Int, Int>? {
        var x = start.first + dir.first
        var y = start.second + dir.second

        while (isInArray(x, y)) {
            when (data.enemyShotData[x][y]) {
                "0" -> return x to y
                "x" -> {
                    x += dir.first
                    y += dir.second
                }
                "*" -> return null
            }
        }

        return null
    }

    private fun shootCell(x: Int, y: Int): Boolean {
        if (!isInArray(x, y)) return false
        if (data.enemyShotData[x][y] != "0") return false

        return if (data.playerShipData[x][y] != "0") {
            data.enemyShotData[x][y] = "x"

            handleHit(x, y)
            checkShipDestroyed(data.playerShipData[x][y], x, y)

            true
        } else {
            data.enemyShotData[x][y] = "*"
            true
        }
    }

    private fun handleHit(x: Int, y: Int) {
        val hit = x to y

        if (firstHit == null) {
            firstHit = hit
            addNearbyTargets(x, y)
            return
        }

        val first = firstHit!!

        if (direction == null) {
            direction = when {
                x == first.first -> 0 to (if (y > first.second) 1 else -1)
                y == first.second -> (if (x > first.first) 1 else -1) to 0
                else -> null
            }

            targets.clear()
        }
    }

    private fun addNearbyTargets(x: Int, y: Int) {
        val possibleTargets = listOf(
            x - 1 to y,
            x to y + 1,
            x + 1 to y,
            x to y - 1
        )

        for (target in possibleTargets.shuffled()) {
            if (
                isInArray(target.first, target.second) &&
                data.enemyShotData[target.first][target.second] == "0"
            ) {
                targets.add(target)
            }
        }
    }

    private fun checkShipDestroyed(shipData: String, x: Int, y: Int) {
        val length = shipData.toIntOrNull() ?: return
        var shipHealth = length - 1

        var checkUp = true
        var checkRight = true
        var checkDown = true
        var checkLeft = true

        for (i in 1..length) {
            if (checkUp) {
                if (isInArray(x - i, y) && data.enemyShotData[x - i][y] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        data.totalPlayerShipHealth -= length
                        markAroundDestroyedShipColumn(x, y, length)
                        resetSmartTargeting()
                        return
                    }
                } else {
                    checkUp = false
                }
            }

            if (checkRight) {
                if (isInArray(x, y + i) && data.enemyShotData[x][y + i] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        data.totalPlayerShipHealth -= length
                        markAroundDestroyedShipLine(x, y, length)
                        resetSmartTargeting()
                        return
                    }
                } else {
                    checkRight = false
                }
            }

            if (checkDown) {
                if (isInArray(x + i, y) && data.enemyShotData[x + i][y] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        data.totalPlayerShipHealth -= length
                        markAroundDestroyedShipColumn(x, y, length)
                        resetSmartTargeting()
                        return
                    }
                } else {
                    checkDown = false
                }
            }

            if (checkLeft) {
                if (isInArray(x, y - i) && data.enemyShotData[x][y - i] == "x") {
                    shipHealth--
                    if (shipHealth == 0) {
                        data.totalPlayerShipHealth -= length
                        markAroundDestroyedShipLine(x, y, length)
                        resetSmartTargeting()
                        return
                    }
                } else {
                    checkLeft = false
                }
            }
        }
    }

    private fun markAroundDestroyedShipLine(x: Int, y: Int, length: Int) {
        var startY = y

        for (i in 0 until length) {
            if (isInArray(x, y - i) && data.enemyShotData[x][y - i] == "x") {
                startY = y - i
            }
        }

        for (u in 0 until length) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    val targetX = x + dx
                    val targetY = startY + u + dy

                    if (
                        isInArray(targetX, targetY) &&
                        data.enemyShotData[targetX][targetY] == "0"
                    ) {
                        data.enemyShotData[targetX][targetY] = "*"
                    }
                }
            }
        }
    }

    private fun markAroundDestroyedShipColumn(x: Int, y: Int, length: Int) {
        var startX = x

        for (i in 0 until length) {
            if (isInArray(x - i, y) && data.enemyShotData[x - i][y] == "x") {
                startX = x - i
            }
        }

        for (u in 0 until length) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    val targetX = startX + u + dx
                    val targetY = y + dy

                    if (
                        isInArray(targetX, targetY) &&
                        data.enemyShotData[targetX][targetY] == "0"
                    ) {
                        data.enemyShotData[targetX][targetY] = "*"
                    }
                }
            }
        }
    }

    private fun resetSmartTargeting() {
        firstHit = null
        direction = null
        targets.clear()
    }

    private fun markAroundDestroyedShip(shipData: String) {
        for (x in 0 until data.getArraySize()) {
            for (y in 0 until data.getArraySize()) {
                if (
                    data.playerShipData[x][y] == shipData &&
                    data.enemyShotData[x][y] == "x"
                ) {
                    markAroundCell(x, y)
                }
            }
        }
    }

    private fun markAroundCell(x: Int, y: Int) {
        for (dx in -1..1) {
            for (dy in -1..1) {
                val nx = x + dx
                val ny = y + dy

                if (
                    isInArray(nx, ny) &&
                    data.enemyShotData[nx][ny] == "0"
                ) {
                    data.enemyShotData[nx][ny] = "*"
                }
            }
        }
    }

    private fun findKnownShipTarget(): Pair<Int, Int>? {
        for (x in 0 until data.getArraySize()) {
            for (y in 0 until data.getArraySize()) {
                if (
                    data.playerShipData[x][y] != "0" &&
                    data.enemyShotData[x][y] == "0"
                ) {
                    return x to y
                }
            }
        }

        return null
    }

    private fun isInArray(x: Int, y: Int): Boolean {
        return x in 0 until data.getArraySize() &&
                y in 0 until data.getArraySize()
    }
}