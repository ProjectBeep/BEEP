package com.lighthouse.beep.ui.feature.editor.behavior

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class HorizontalHeaderLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    var onOffsetChangedListener: OnOffsetChangedListener? = null
        private set

    @JvmName("setOnOffsetChanged")
    fun setOnOffsetChangedListener(listener: OnOffsetChangedListener?) {
        onOffsetChangedListener = listener
    }
}