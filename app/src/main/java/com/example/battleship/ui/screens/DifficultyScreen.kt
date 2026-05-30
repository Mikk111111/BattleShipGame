package com.example.battleship.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.battleship.game.GameController
import com.example.battleship.ui.components.BackgroundImage
import androidx.compose.ui.platform.LocalContext
import com.example.battleship.audio.SoundManager

@Composable
fun DifficultyScreen(
    onDifficultySelected: (GameController.Difficulty) -> Unit,
    onBackClick: () -> Unit
) {
    val buttonWidth = 180.dp
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundImage()

        Card(
            modifier = Modifier.align(Alignment.Center),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.35f)
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Difficulty",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        SoundManager.playClick(context)
                        onDifficultySelected(GameController.Difficulty.EASY)
                    },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Easy")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        SoundManager.playClick(context)
                        onDifficultySelected(GameController.Difficulty.NORMAL)
                    },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Normal")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        SoundManager.playClick(context)
                        onDifficultySelected(GameController.Difficulty.HARD)
                    },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Hard")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        SoundManager.playClick(context)
                        onBackClick()
                              },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Back")
                }
            }
        }
    }
}