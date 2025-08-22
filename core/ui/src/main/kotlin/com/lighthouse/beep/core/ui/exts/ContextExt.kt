package com.lighthouse.beep.core.ui.exts

import android.content.Context
import androidx.annotation.StringRes

fun Context.getString(text: String, @StringRes resId: Int, default: String = ""): String {
    if (text.isNotEmpty()) {
        return text
    }
    if (resId != 0) {
        return getString(resId)
    }
    return default
}