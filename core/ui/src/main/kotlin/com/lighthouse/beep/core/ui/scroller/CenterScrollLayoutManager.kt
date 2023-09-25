package com.lighthouse.beep.core.ui.scroller

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


class CenterScrollLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    private val smoothScroller: CenterSmoothScroller

    init {
        smoothScroller = CenterSmoothScroller(context, orientation)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    private class CenterSmoothScroller(context: Context?, private val orientation: Int) :
        LinearSmoothScroller(context) {
        override fun calculateTimeForScrolling(dx: Int): Int {
            var duration = super.calculateTimeForScrolling(dx)
            if (duration < DURATION) {
                duration = DURATION
            }
            return duration
        }

        override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
            if (view.parent == null || orientation == VERTICAL) {
                return super.calculateDxToMakeVisible(view, snapPreference)
            }
            val parentWidth = (view.parent as View).width
            val left = (parentWidth - view.width) / 2
            return calculateDtToFit(view.left, view.right, left, left + view.width, snapPreference)
        }

        override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {
            if (view.parent == null || orientation != VERTICAL) {
                return super.calculateDxToMakeVisible(view, snapPreference)
            }
            val parentHeight = (view.parent as View).height
            val top = (parentHeight - view.height) / 2
            return calculateDtToFit(view.top, view.bottom, top, top + view.height, snapPreference)
        }
    }

    companion object {
        private const val DURATION = 150
    }
}