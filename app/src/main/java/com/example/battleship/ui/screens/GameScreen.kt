package com.example.battleship.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.battleship.game.GameController
import com.example.battleship.ui.components.BoardGrid
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.ui.graphics.graphicsLayer

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameScreen(
    gameController: GameController,
    playerName: String,
    onPlayerWon: () -> Unit,
    onBackToMenu: () -> Unit
) {
    var refresh by remember { mutableIntStateOf(0) }
    var scoreSaved by remember { mutableStateOf(false) }

    val winner = gameController.getWinner()

    if (winner == "Player" && !scoreSaved) {
        onPlayerWon()
        scoreSaved = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
    ) {

        key(refresh) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val edgeDividerWidth = maxWidth * 0.025f
                val hpBarWidth = maxWidth * 0.025f
                val centerDividerWidth = maxWidth * 0.025f
                val spacing = 0.dp

                val titleHeight = 24.dp
                val verticalPadding = 16.dp

                val availableBoardWidth =
                    (maxWidth
                            - edgeDividerWidth * 2
                            - hpBarWidth * 2
                            - centerDividerWidth
                            - spacing * 6
                            ) / 2

                val availableBoardHeight =
                    maxHeight - titleHeight - verticalPadding

                val rawBoardSize =
                    minOf(availableBoardWidth, availableBoardHeight)

                val cellSize = rawBoardSize / 10
                val boardSize = maxHeight - titleHeight

                val totalContentWidth =
                    edgeDividerWidth * 2 +
                            hpBarWidth * 2 +
                            centerDividerWidth +
                            boardSize * 2 +
                            spacing * 6

                Row(
                    modifier = Modifier
                        .width(totalContentWidth)
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    VerticalDivider(
                        width = edgeDividerWidth,
                        height = boardSize + titleHeight
                    )

                    Spacer(modifier = Modifier.width(spacing))

                    HpBar(
                        hp = gameController.data.totalPlayerShipHealth,
                        maxHp = gameController.data.maxPlayerShipHealth,
                        height = boardSize + titleHeight,
                        width = hpBarWidth
                    )

                    Spacer(modifier = Modifier.width(spacing))

                    BoardWithTitle(
                        modifier = Modifier.weight(1f),
                        title = "Your board",
                        boardSize = boardSize,
                        shots = gameController.data.enemyShotData,
                        ships = gameController.data.playerShipData,
                        showShips = true,
                        clickable = false
                    )

                    Spacer(modifier = Modifier.width(spacing))

                    VerticalDivider(
                        width = centerDividerWidth,
                        height = boardSize + titleHeight
                    )

                    Spacer(modifier = Modifier.width(spacing))

                    BoardWithTitle(
                        modifier = Modifier.weight(1f),
                        title = "Enemy board",
                        boardSize = boardSize,
                        shots = gameController.data.playerShotData,
                        ships = gameController.data.enemyShipData,
                        showShips = false,
                        clickable = winner == null,
                        onCellClick = { x, y ->
                            gameController.playerShoot(x, y)
                            refresh++
                        }
                    )

                    Spacer(modifier = Modifier.width(spacing))

                    HpBar(
                        hp = gameController.data.totalEnemyShipHealth,
                        maxHp = gameController.data.maxEnemyShipHealth,
                        height = boardSize + titleHeight,
                        width = hpBarWidth
                    )

                    Spacer(modifier = Modifier.width(spacing))

                    VerticalDivider(
                        width = edgeDividerWidth,
                        height = boardSize + titleHeight
                    )
                }
            }
        }

        if (winner != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Card {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (winner == "Player") {
                            Text("Victory!")
                            Text("Player: $playerName")
                            Text("Score: ${gameController.data.score}")
                            Text("Turns: ${gameController.data.turns}")
                        } else {
                            Text("Game Over")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = onBackToMenu) {
                            Text("Main Menu")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoardWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    boardSize: Dp,
    shots: Array<Array<String>>,
    ships: Array<Array<String>>,
    showShips: Boolean,
    clickable: Boolean,
    onCellClick: (Int, Int) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier.width(boardSize)
    ) {
        Box(
            modifier = Modifier
                .width(boardSize)
                .height(24.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(title)
        }

        BoardGrid(
            boardSize = boardSize,
            shots = shots,
            ships = ships,
            showShips = showShips,
            clickable = clickable,
            onCellClick = onCellClick
        )
    }
}

@Composable
fun HpBar(
    hp: Int,
    maxHp: Int,
    height: Dp,
    width: Dp
) {
    val safeMaxHp = maxHp.coerceAtLeast(1)
    val safeHp = hp.coerceIn(0, safeMaxHp)
    val ratio = safeHp.toFloat() / safeMaxHp.toFloat()
    val percent = (ratio * 100).toInt()

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color.DarkGray),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height * ratio)
                .background(Color.Red)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$percent%",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
                    .requiredWidth(height)
                    .graphicsLayer {
                        rotationZ = 90f
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VerticalDivider(
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color.Gray)
    )
}