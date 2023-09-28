package com.lighthouse.beep.ui.feature.editor.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.marginStart
import kotlin.math.max
import kotlin.math.min

class HorizontalScrollBehavior(
    context: Context,
    attrs: AttributeSet
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is HorizontalHeaderLayout) {
            return true
        }
        return false
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is HorizontalHeaderLayout) {
            ViewCompat.offsetLeftAndRight(child, dependency.right - child.left)
            return true
        }
        return false
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val header = findHeaderLayout(parent)
        val headerStartMargin = header?.marginStart ?: 0
        val headerMinWidth = header?.minWidth ?: 0

        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            parent.measuredWidth - headerStartMargin - headerMinWidth,
            MeasureSpec.EXACTLY,
        )

        parent.onMeasureChild(
            child,
            childWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )

        return true
    }

    private fun findHeaderLayout(parent: CoordinatorLayout): HorizontalHeaderLayout? {
        for(child in parent.children) {
            if(child is HorizontalHeaderLayout) {
                return child
            }
        }
        return null
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_HORIZONTAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        val header = findHeaderLayout(coordinatorLayout) ?: return
        if(dx > 0 && header.width > header.minWidth) {
            val delta = min(dx, header.width - header.minWidth)
            consumed[0] = delta
            scroll(header, delta)
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        val header = findHeaderLayout(coordinatorLayout) ?: return
        if(dxUnconsumed < 0 && header.width < header.measuredWidth) {
            val delta = max(dxUnconsumed, header.width - header.measuredWidth)
            consumed[0] = delta
            scroll(header, delta)
        }
    }

    private fun scroll(header: HorizontalHeaderLayout, delta: Int) {
        if (delta == 0) {
            return
        }
        val newRight = header.left + header.width - delta
        header.layout(header.left, header.top, newRight, header.bottom)
        val total = header.measuredWidth - header.minWidth
        val current = header.width - header.minWidth
        if (total == 0) {
            header.onOffsetChangedListener?.onChanged(0, 1f)
        } else {
            header.onOffsetChangedListener?.onChanged(current - total, current.toFloat() / total)
        }
    }
}