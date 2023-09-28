package com.lighthouse.beep.core.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearItemDecoration(
    private val space: Int,
    private val start: Int = 0,
    private val end: Int = 0,
) : RecyclerView.ItemDecoration() {

    constructor(space: Float): this(space.toInt())

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val manager = parent.layoutManager as? LinearLayoutManager ?: return
        val itemCount = parent.adapter?.itemCount ?: 0
        val position = getPosition(parent.getChildAdapterPosition(view), itemCount)

        if (manager.orientation == RecyclerView.HORIZONTAL) {
            horizontalItemOffsets(outRect, position)
        } else {
            verticalItemOffsets(outRect, position)
        }
    }

    private fun getPosition(pos: Int, itemCount: Int): Position {
        return when (pos) {
            0 -> Position.Start
            itemCount - 1 -> Position.End
            else -> Position.Mid
        }
    }

    private fun horizontalItemOffsets(outRect: Rect, position: Position) {
        outRect.left = when (position) {
            Position.Start -> start
            else -> space / 2
        }
        outRect.top = 0
        outRect.right = when (position) {
            Position.End -> end
            else -> space / 2
        }
        outRect.bottom = 0
    }

    private fun verticalItemOffsets(outRect: Rect, position: Position) {
        outRect.left = 0
        outRect.top = when (position) {
            Position.Start -> start
            else -> space / 2
        }
        outRect.right = 0
        outRect.bottom = when (position) {
            Position.End -> end
            else -> space / 2
        }
    }

    private enum class Position {
        Start, End, Mid
    }
}