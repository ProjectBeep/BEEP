package com.lighthouse.beep.core.common.exts

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.calculateDDay(): Int {
    val oneDay = 24 * 60 * 60 * 1000L
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    Log.d("DATE", formatter.format(this))

    return (time / oneDay).toInt() - (System.currentTimeMillis() / oneDay).toInt()
}

fun Date.calculateNextDayRemainingTime(): Long {
    val oneDay = 24 * 60 * 60 * 1000L
    val nextDay = time + oneDay
    return nextDay - nextDay % oneDay - time
}