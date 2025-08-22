package com.lighthouse.beep.core.ui.recyclerview.itemtouch

import android.graphics.PointF
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

abstract class OnLongPressDragListener: RecyclerView.OnItemTouchListener {
    protected var downPosition: Int = -1
        private set
    private var downPos: PointF = PointF()

    protected var movePosition: Int = -1
        private set

    private var isLongPressCanceled = true

    private var job: Job? = null
    private var scrollDy = 0

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when(e.action) {
            MotionEvent.ACTION_DOWN -> {
                downPosition = getPosition(rv, e)
                if(downPosition == -1) {
                    return false
                }
                downPos.set(e.x, e.y)
                isLongPressCanceled = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (isLongPressCanceled) {
                    return false
                }

                val touchSlop = ViewConfiguration.get(rv.context).scaledTouchSlop
                val diffX = abs(e.x - downPos.x)
                val diffY = abs(e.y - downPos.y)
                if (diffX > touchSlop || diffY > touchSlop) {
                    isLongPressCanceled = true
                    return false
                }

                val currentPosition = getPosition(rv, e)
                if (downPosition != currentPosition || currentPosition == -1) {
                    isLongPressCanceled = true
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