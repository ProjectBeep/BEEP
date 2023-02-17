package com.lighthouse.features.common.binding

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lighthouse.core.android.utils.resource.UIText

@BindingAdapter("setUIText")
fun TextView.setUIText(uiText: UIText?) {
    if (uiText == null) return
    if (uiText.clickable) {
        movementMethod = LinkMovementMethod()
    }
    text = uiText.asString(context)
}
