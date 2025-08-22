package com.lighthouse.beep.core.common.exts

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val EMPTY_DATE = Date(0)

fun Date.calculateDDay(): Int {
    val oneDay = 24 * 60 * 60 * 1000L
    return (time / oneDay).toInt() - (System.currentTimeMillis() / oneDay).toInt()
}

fun Date.toFormattedString(pattern: String = "yyyy.MM.dd"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

fun String.toDate(pattern: String = "yyyy.MM.dd"): Date {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.parse(this) ?: Date(0)
}

fun Date.calculateNextDayRemainingTime(): Long {
    val oneDay = 24 * 60 * 60 * 1000L
    val nextDay = time + oneDay
    return nextDay - nextDay % oneDay - time
}

fun Date.ofYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

fun Date.ofMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH) + 1
}

fun Date.ofDate(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DATE)
}