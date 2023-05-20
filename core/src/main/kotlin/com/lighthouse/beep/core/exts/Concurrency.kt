package com.lighthouse.beep.core.exts

import java.text.DecimalFormat

fun String.toConcurrency(): String {
    return DecimalFormat("#,###").format(toDigit())
}

fun Int.toConcurrency(): String {
    return DecimalFormat("#,###").format(this)
}
