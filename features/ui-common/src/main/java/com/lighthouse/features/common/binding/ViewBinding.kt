package com.lighthouse.features.common.binding

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("setVisible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}
