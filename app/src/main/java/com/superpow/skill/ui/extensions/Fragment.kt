package com.superpow.skill.ui.extensions

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.superpow.skill.ui.core.Storage
import java.io.IOException

internal inline fun Fragment.handleBackPress(
    crossinline onBackPressed: () -> Unit,
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        },
    )
}

@SuppressLint("SourceLockedOrientationActivity")
fun Fragment.setPortrait() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

@SuppressLint("SourceLockedOrientationActivity")
fun Fragment.setFullSensor() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
}

fun Fragment.playWin() {
    if (Storage.getInstance(requireActivity()).sounds.not()) return
    try {
        val assetManager = requireActivity().assets
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setVolume(1f, 1f)

        val afd = assetManager.openFd("winned.mp3")
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.isLooping = false
        mediaPlayer.prepare()
        mediaPlayer.start()
    } catch (ex: IOException) {
        Log.e("MediaPlayer", ex.localizedMessage, ex)
    }
}

@Suppress("DEPRECATION")
fun Fragment.vibrate(duration: Long = 150L) {
    if (Storage.getInstance(requireActivity()).vibrates.not()) return
    try {
        val vibrator = ContextCompat.getSystemService(requireContext(), Vibrator::class.java)
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        255
                    ),
                )
            } else {
                vibrator.vibrate(duration)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
