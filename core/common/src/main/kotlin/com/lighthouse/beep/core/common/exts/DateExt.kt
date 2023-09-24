package com.lighthouse.beep.core.common.exts

import java.util.Date

fun Date.calculateDDay(): Int {
    val oneDay = 24 * 60 * 60 * 1000L
    val today = System.currentTimeMillis() / oneDay
    return (time / oneDay - today).toInt()
}