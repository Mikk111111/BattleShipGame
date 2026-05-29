package com.example.battleship.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.battleship.data.ScoreEntry
import com.example.battleship.ui.components.BackgroundImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LeaderboardScreen(
    scores: List<ScoreEntry>,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundImage()

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.85f),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.35f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Leaderboard", color = Color.White)

                Spacer(modifier = Modifier.height(16.dp))

                if (scores.isEmpty()) {
                    Text("No scores yet", color = Color.White)
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        itemsIndexed(scores) { index, score ->
                            val dateText =
                                SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                    .format(Date(score.timestamp))

                            Text(
                                text = "${index + 1}. ${score.name} - ${score.score} pts - ${score.turns} turns - ${score.difficulty} - $dateText",
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onBackClick) {
                    Text("Back")
                }
            }
        }
    }
}