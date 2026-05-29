package com.example.battleship

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.battleship.data.ScoreEntry
import com.example.battleship.data.ScoreStorage
import com.example.battleship.game.GameController
import com.example.battleship.ui.screens.DifficultyScreen
import com.example.battleship.ui.screens.GameScreen
import com.example.battleship.ui.screens.LeaderboardScreen
import com.example.battleship.ui.screens.StartScreen
import com.example.battleship.ui.theme.BattleShipTheme
import com.example.battleship.data.SettingsStorage
import com.example.battleship.ui.screens.PlayerNameScreen
import android.view.WindowInsets
import androidx.annotation.RequiresApi

class MainActivity : ComponentActivity() {

    private val gameController = GameController()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsStorage = SettingsStorage(this)
        val scoreStorage = ScoreStorage(this)
        enableEdgeToEdge()

        window.insetsController?.hide(
            WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
        )

        setContent {
            BattleShipTheme {
                var screen by remember { mutableStateOf("start") }

                when (screen) {
                    "start" -> StartScreen(
                        onStartClick = { screen = "difficulty" },
                        onPlayerNameClick = { screen = "playerName" },
                        onScoreClick = { screen = "leaderboard" },
                        onExitClick = { finish() }
                    )

                    "playerName" -> PlayerNameScreen(
                        currentName = settingsStorage.getPlayerName(),
                        onSaveClick = { name ->
                            settingsStorage.savePlayerName(name)
                            screen = "start"
                        },
                        onBackClick = { screen = "start" }
                    )

                    "difficulty" -> DifficultyScreen(
                        onDifficultySelected = { difficulty ->
                            gameController.startNewGame(difficulty)
                            screen = "game"
                        },
                        onBackClick = { screen = "start" }
                    )

                    "game" -> GameScreen(
                        gameController = gameController,
                        playerName = settingsStorage.getPlayerName(),
                        onPlayerWon = {
                            scoreStorage.saveScore(
                                ScoreEntry(
                                    name = settingsStorage.getPlayerName(),
                                    score = gameController.data.score,
                                    turns = gameController.data.turns,
                                    difficulty = gameController.currentDifficulty.name,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                        },
                        onBackToMenu = { screen = "start" }
                    )

                    "leaderboard" -> LeaderboardScreen(
                        scores = scoreStorage.getScores(),
                        onBackClick = { screen = "start" }
                    )
                }
            }
        }
    }
}