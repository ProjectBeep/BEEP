package com.lighthouse.beep.core.exts

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import java.io.IOException

fun Uri?.exists(context: Context): Boolean {
    this ?: return false
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
                cursor.moveToFirst()
            } ?: false
        }

        ContentResolver.SCHEME_FILE -> toFile().exists()
        else -> throw IOException("알 수 없는 scheme 입니다.")
    }
}
