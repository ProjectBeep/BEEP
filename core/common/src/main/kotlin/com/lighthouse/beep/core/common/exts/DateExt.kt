package com.lighthouse.beep.core.common.exts

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.calculateDDay(): Int {
    val oneDay = 24 * 60 * 60 * 1000L
    return (time / oneDay).toInt() - (System.currentTimeMillis() / oneDay).toInt()
}

fun Date.toFormattedString(pattern: String = "yyyy.MM.dd"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

fun Date.calculateNextDayRemainingTime(): Long {
    val oneDay = 24 * 60 * 60 * 1000L
    val nextDay = time + oneDay
    return nextDay - nextDay % oneDay - time
}