package com.lighthouse.beep.core.common.exts

import android.content.res.Resources

val displayWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

val displayHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels