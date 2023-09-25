package com.lighthouse.beep.ui.feature.home.decorator

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.lighthouse.beep.core.ui.exts.dp

internal class HomeItemDecoration(
    private val callback: HomeItemDecorationCallback,
) : ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        callback.onTopItemPosition(topChildPosition)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount ?: 0
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition < callback.getExpiredGifticonFirstIndex()) {
            return
        }

        outRect.left = 20.dp
        outRect.top = when (adapterPosition) {
            callback.getExpiredGifticonFirstIndex() -> 0.dp
            else -> 4.dp
        }
        outRect.right = 20.dp
        outRect.bottom = when(adapterPosition) {
            itemCount - 1 -> 0.dp
            else -> 4.dp
        }
    }
}