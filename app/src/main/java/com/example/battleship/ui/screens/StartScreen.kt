package com.example.battleship.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.battleship.ui.components.BackgroundImage
import androidx.compose.material3.CardDefaults

@Composable
fun StartScreen(
    onStartClick: () -> Unit,
    onPlayerNameClick: () -> Unit,
    onScoreClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundImage()

        val buttonWidth = 180.dp

        Card(
            modifier = Modifier.align(Alignment.Center),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.35f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Battleship", color = Color.White)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onStartClick,
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Start")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPlayerNameClick,
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Player Name")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onScoreClick,
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Score")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onExitClick,
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Exit")
                }
            }
        }
    }
}