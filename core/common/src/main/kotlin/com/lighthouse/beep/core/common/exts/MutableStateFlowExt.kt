package com.lighthouse.beep.core.common.exts

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<List<T>>.removeAt(index: Int) {
    if (index != -1) {
        value = value.removedAt(index)
    }
}

fun <T> MutableStateFlow<List<T>>.remove(predicate: (T) -> Boolean) {
    value = value.removed(predicate)
}

fun <T> MutableStateFlow<List<T>>.add(item: T) {
    value = value + listOf(item)
}