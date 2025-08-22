package com.lighthouse.beep.core.ui.recyclerview.decoration

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
        val n = manager.spanCount
        val i = getSpanIndex(manager, position)

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

    private fun getSpanIndex(manager: GridLayoutManager, position: Int): Int {
        return manager.spanSizeLookup.getSpanIndex(position, manager.spanCount)
    }
}