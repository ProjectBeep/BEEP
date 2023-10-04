package com.lighthouse.beep.core.ui.utils.vibrator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference

object VibratorGenerator {

    private var vibratorRef = WeakReference<Vibrator>(null)
    private var vibratorManagerRef = WeakReference<VibratorManager>(null)

    @SuppressLint("ObsoleteSdkInt", "MissingPermission")
    fun vibrate(context: Context, duration: Long = 400L) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = obtainVibratorManager(context)
            val vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            val combinedVibration = CombinedVibration.createParallel(vibrationEffect)
            vibratorManager.vibrate(combinedVibration)
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrator = obtainVibrator(context)
            val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            val vibrator = obtainVibrator(context)
            vibrator.vibrate(duration)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun obtainVibratorManager(context: Context): VibratorManager {
        vibratorManagerRef.get()?.let {
            return it
        }
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        return vibratorManager.also {
            vibratorManagerRef = WeakReference(it)
        }
    }

    private fun obtainVibrator(context: Context): Vibrator {
        vibratorRef.get()?.let {
            return it
        }
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return vibrator.also {
            vibratorRef = WeakReference(it)
        }
    }
}