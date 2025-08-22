package com.lighthouse.beep.core.common.exts

inline fun <reified T> Any.cast(): T {
    return this as? T ?: throw RuntimeException("${T::class.simpleName} should not be null")
}