package com.lighthouse.beep.core.common.exts

fun <T> List<T>.removedAt(index: Int): List<T> {
    if (index == -1){
        return this
    }
    return subList(0, index) + subList(index + 1, size)
}

fun <T> List<T>.removed(predicate: (T) -> Boolean): List<T> {
    val index = indexOfFirst(predicate)

    return removedAt(index)
}