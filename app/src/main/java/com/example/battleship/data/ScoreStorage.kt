package com.example.battleship.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit

class ScoreStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences("scores", Context.MODE_PRIVATE)

    fun saveScore(entry: ScoreEntry) {
        val scores = getScores().toMutableList()
        scores.add(entry)

        val array = JSONArray()

        scores.forEach {
            val obj = JSONObject()
            obj.put("name", it.name)
            obj.put("score", it.score)
            obj.put("turns", it.turns)
            obj.put("difficulty", it.difficulty)
            obj.put("timestamp", it.timestamp)
            array.put(obj)
        }

        prefs.edit() {
            putString("leaderboard", array.toString())
        }
    }

    fun getScores(): List<ScoreEntry> {
        val json = prefs.getString("leaderboard", "[]") ?: "[]"
        val array = JSONArray(json)

        val scores = mutableListOf<ScoreEntry>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            scores.add(
                ScoreEntry(
                    name = obj.getString("name"),
                    score = obj.getInt("score"),
                    turns = obj.getInt("turns"),
                    difficulty = obj.optString("difficulty", "NORMAL"),
                    timestamp = obj.optLong("timestamp", 0L)
                )
            )
        }

        return scores.sortedByDescending { it.score }
    }
}