package com.lighthouse.beep.core.common.exts

fun String.toDigit(): Int {
    return filter { it.isDigit() }.toIntOrNull() ?: 0
}
