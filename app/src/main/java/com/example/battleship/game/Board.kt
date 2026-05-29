package com.example.battleship.game

class Board(
    val size: Int = 10
) {
    val cells: Array<Array<String>> = Array(size) { Array(size) { "." } }

    fun getCell(x: Int, y: Int): String {
        return cells[x][y]
    }

    fun setCell(x: Int, y: Int, value: String) {
        cells[x][y] = value
    }

    fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until size && y in 0 until size
    }
}