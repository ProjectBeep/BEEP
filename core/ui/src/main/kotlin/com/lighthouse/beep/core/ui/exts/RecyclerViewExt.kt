package com.lighthouse.beep.core.ui.exts

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.ui.model.ScrollInfo

fun RecyclerView.getScrollInfo(getViewSpace: (position:Int) -> Int = { 0 }): ScrollInfo {
    val manager = layoutManager as? LinearLayoutManager ?: return ScrollInfo.None
    val position = manager.findFirstVisibleItemPosition()
    val viewOffset = if (manager.orientation == RecyclerView.VERTICAL) {
        manager.findViewByPosition(position)?.top ?: 0
    } else {
        manager.findViewByPosition(position)?.left ?: 0
    }
    val viewSpace = getViewSpace(position)
    val padding = if (manager.orientation == RecyclerView.VERTICAL) {
        paddingTop
    } else {
        paddingLeft
    }
    return ScrollInfo(position, viewOffset - viewSpace - padding)
}