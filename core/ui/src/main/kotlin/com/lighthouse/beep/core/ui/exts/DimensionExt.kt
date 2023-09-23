package com.lighthouse.beep.core.ui.exts

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

val Int.dp
    get() = Resources.getSystem().displayMetrics.density.let{ density ->
        (this * density).roundToInt()
    }

val Float.dp
    get() = Resources.getSystem().displayMetrics.density.let{ density ->
        this * density
    }

val Int.sp
    get() = Resources.getSystem().displayMetrics.let{ metrics ->
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), metrics)
    }

val Float.sp
    get() = Resources.getSystem().displayMetrics.let{ metrics ->
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, metrics)
    }