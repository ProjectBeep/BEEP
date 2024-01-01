package com.lighthouse.beep.core.ui.recyclerview

import android.content.res.Resources

object GridCalculator {

    fun getSpanCount(minViewSize: Int, space: Int, paddingHorizontal: Int, minSpanCount: Int): Int {
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        var n = 0
        var curSize = 0
        while (curSize < displayWidth) {
            n++
            curSize = paddingHorizontal + minViewSize * n + space * (n - 1)
        }
        return maxOf(n - 1, minSpanCount)
    }

    fun getRowCount(minViewSize: Int, space: Int): Int {
        val displayHeight = Resources.getSystem().displayMetrics.heightPixels
        var n = 0
        var curSize = 0
        while (curSize < displayHeight) {
            n++
            curSize = minViewSize * n + space * (n - 1)
        }
        return n - 1
    }
}