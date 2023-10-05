package com.lighthouse.beep.core.ui.recyclerview.scroll

import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class OnLongPressDragListener: RecyclerView.OnItemTouchListener {
    protected var downPosition: Int = -1
        private set
    protected var movePosition: Int = -1
        private set

    private var isSameItemPressed = false

    private var job: Job? = null
    private var scrollDy = 0

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when(e.action) {
            MotionEvent.ACTION_DOWN -> {
                downPosition = getPosition(rv, e)
                if(downPosition == -1) {
                    return false
                }
                isSameItemPressed = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isSameItemPressed) {
                    return false
                }
                val currentPosition = getPosition(rv, e)
                if (downPosition != currentPosition || currentPosition == -1) {
                    isSameItemPressed = false
                    return false
                }
                if (e.eventTime - e.downTime >= ViewConfiguration.getLongPressTimeout()) {
                    job = startDrag(rv)
                    return job != null
                }
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        when(e.action) {
            MotionEvent.ACTION_MOVE -> {
                moveDrag(getPosition(rv, e))
                scrollDy = when {
                    0 > e.y -> e.y.toInt() / 10
                    rv.height < e.y -> (e.y - rv.height).toInt() / 10
                    else -> 0
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                onEndDrag()
                job?.cancel()
                job = null
            }
        }
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit

    private fun getPosition(rv: RecyclerView, event: MotionEvent): Int {
        val view = rv.findChildViewUnder(event.x, event.y) ?: return -1
        return rv.getChildAdapterPosition(view)
    }

    private fun startDrag(rv: RecyclerView): Job? {
        if (!onStartDrag()) {
            return null
        }
        movePosition = downPosition
        return rv.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            while(isActive) {
                delay(5)
                if (scrollDy != 0) {
                    rv.scrollBy(0, scrollDy)
                }
            }
        }
    }

    private fun moveDrag(position: Int) {
        if (position == -1 || movePosition == position) {
            return
        }
        onMoveDrag(position)
        movePosition = position
    }

    abstract fun onStartDrag() : Boolean

    abstract fun onMoveDrag(position: Int)

    abstract fun onEndDrag()
}