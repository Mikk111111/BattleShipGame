package com.example.battleship.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.battleship.ui.components.BackgroundImage

@Composable
fun PlayerNameScreen(
    currentName: String,
    onSaveClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    val buttonWidth = 180.dp

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
                    text = "Player Name",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onSaveClick(name.trim())
                        }
                    },
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Save")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onBackClick,
                    modifier = Modifier.width(buttonWidth)
                ) {
                    Text("Back")
                }
            }
        }
    }
}