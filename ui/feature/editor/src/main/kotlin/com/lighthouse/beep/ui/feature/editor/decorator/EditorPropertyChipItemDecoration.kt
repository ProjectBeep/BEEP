package com.lighthouse.beep.ui.feature.editor.decorator

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.ui.feature.editor.R

class EditorPropertyChipItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val decoration =
        ContextCompat.getDrawable(context, R.drawable.editor_property_chip_decoration)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val decorationWidth = decoration?.intrinsicWidth ?: 0
        decoration?.setBounds(0, parent.top, decorationWidth, parent.bottom)
        decoration?.draw(c)
    }
}