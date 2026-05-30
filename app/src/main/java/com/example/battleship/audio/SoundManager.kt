package com.example.battleship.audio

import android.content.Context
import android.media.MediaPlayer
import com.example.battleship.R

object SoundManager {

    private fun play(context: Context, resId: Int) {
        val player = MediaPlayer.create(context, resId)

        player.setOnCompletionListener {
            it.release()
        }

        player.start()
    }

    fun playClick(context: Context) =
        play(context, R.raw.click)

    fun playHit(context: Context) =
        play(context, R.raw.hit)

    fun playMiss(context: Context) =
        play(context, R.raw.miss)

    fun playVictory(context: Context) =
        play(context, R.raw.victory)
}