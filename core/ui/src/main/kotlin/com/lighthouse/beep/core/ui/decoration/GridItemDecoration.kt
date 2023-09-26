package com.lighthouse.beep.core.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val space: Float
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return

        val manager = parent.layoutManager as? GridLayoutManager ?: return
        val n = getGroupSize(manager, position)
        val i = getGroupIndex(manager, position)

        val isVertical = manager.orientation == GridLayoutManager.VERTICAL
        if (isVertical) {
            if (position >= n) {
                outRect.top = space.toInt()
            }
            outRect.left = (i * space / n).toInt()
            outRect.right = (space - (i + 1) * space / n).toInt()
        } else {
            if (position >= n) {
                outRect.left = space.toInt()
            }
            outRect.top = (i * space / n).toInt()
            outRect.bottom = (space - (i + 1) * space / n).toInt()
        }
    }

    private fun getGroupSize(manager: GridLayoutManager, position: Int): Int {
        return manager.spanSizeLookup.getSpanSize(position)
    }

    private fun getGroupIndex(manager: GridLayoutManager, position: Int): Int {
        return manager.spanSizeLookup.getSpanGroupIndex(position, manager.spanCount)
    }
}