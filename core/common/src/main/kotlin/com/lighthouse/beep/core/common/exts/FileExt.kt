package com.lighthouse.beep.core.common.exts

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun File.compressBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    FileOutputStream(this).use { output ->
        bitmap.compress(format, quality, output)
    }
}
