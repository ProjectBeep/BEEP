package com.lighthouse.beep.library.barcode

import android.graphics.Bitmap
import android.util.LruCache

internal class BarcodeLruCache(
    cacheSize: Int
) : LruCache<String, Bitmap>(cacheSize) {
    override fun sizeOf(key: String?, value: Bitmap?): Int {
        value ?: return 0
        return value.byteCount / 1024
    }
}