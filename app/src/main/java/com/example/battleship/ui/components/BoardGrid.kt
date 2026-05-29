package com.example.battleship.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.battleship.R
import androidx.compose.ui.layout.ContentScale

@Composable
fun BoardGrid(
    boardSize: Dp,
    shots: Array<Array<String>>,
    ships: Array<Array<String>>,
    showShips: Boolean,
    clickable: Boolean,
    onCellClick: (Int, Int) -> Unit = { _, _ -> }
) {
    val cellSize = boardSize / 10

    Column(
        modifier = Modifier.size(boardSize)
    ) {
        for (x in 0 until 10) {
            Row {
                for (y in 0 until 10) {
                    val shotValue = shots[x][y]
                    val shipValue = ships[x][y]

                    val waterRotation = ((x * 31 + y * 17) % 4) * 90f

                    val up = isShipCell(ships, x - 1, y)
                    val down = isShipCell(ships, x + 1, y)
                    val left = isShipCell(ships, x, y - 1)
                    val right = isShipCell(ships, x, y + 1)

                    val isShip = shipValue != "0"
                    val neighborCount = listOf(up, down, left, right).count { it }
                    val isShipEnd = isShip && neighborCount == 1
                    val isShipMiddle = isShip && neighborCount >= 2

                    val imageId = when {
                        shotValue == "*" -> R.drawable.miss

                        isShip && shotValue == "x" && isShipEnd -> R.drawable.ship_end_hit
                        isShip && shotValue == "x" && isShipMiddle -> R.drawable.ship_middle_hit

                        showShips && isShip && isShipEnd -> R.drawable.ship_end
                        showShips && isShip && isShipMiddle -> R.drawable.ship_middle

                        else -> R.drawable.water
                    }

                    val rotation = when {
                        !isShip && shotValue == "0" -> waterRotation

                        right && !left -> 0f
                        left && !right -> 180f
                        down && !up -> 90f
                        up && !down -> 270f
                        left && right -> 0f
                        up && down -> 90f
                        else -> 0f
                    }

                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(cellSize)
                            .graphicsLayer {
                                rotationZ = rotation
                            }
                            .clickable(enabled = clickable && shotValue == "0") {
                                onCellClick(x, y)
                            }
                    )
                }
            }
        }
    }
}

private fun isShipCell(
    ships: Array<Array<String>>,
    x: Int,
    y: Int
): Boolean {
    if (x !in 0..9 || y !in 0..9) return false
    return ships[x][y] != "0"
}