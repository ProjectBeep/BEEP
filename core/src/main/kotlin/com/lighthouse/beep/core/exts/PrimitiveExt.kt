package com.lighthouse.beep.core.exts

fun String.toDigit(): Int {
    return filter { it.isDigit() }.toIntOrNull() ?: 0
}
